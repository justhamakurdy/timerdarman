package com.example.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.*
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.example.data.*
import com.example.ui.theme.*
import kotlinx.coroutines.*

class OverlayService : Service() {

    private var windowManager: WindowManager? = null
    private var overlayView: View? = null
    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())

    private var lifecycleRegistry: LifecycleRegistry? = null
    private val lifecycleOwner = object : LifecycleOwner, SavedStateRegistryOwner {
        private val _lifecycleRegistry = LifecycleRegistry(this)
        private val savedStateRegistryController = SavedStateRegistryController.create(this)

        override val lifecycle: Lifecycle = _lifecycleRegistry
        override val savedStateRegistry: SavedStateRegistry = savedStateRegistryController.savedStateRegistry

        init {
            lifecycleRegistry = _lifecycleRegistry
            savedStateRegistryController.performRestore(null)
            _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
            _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val medicationId = intent?.getIntExtra("medication_id", -1) ?: -1
        if (medicationId != -1) {
            showOverlay(medicationId)
        }
        return START_NOT_STICKY
    }

    private fun showOverlay(medicationId: Int) {
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        overlayView?.let {
            windowManager?.removeView(it)
        }

        val composeView = ComposeView(this).apply {
            setViewTreeLifecycleOwner(lifecycleOwner)
            setViewTreeSavedStateRegistryOwner(lifecycleOwner)
            setContent {
                DarmanZhmerTheme {
                    OverlayContent(
                        medicationId = medicationId,
                        onDismiss = { stopSelf() },
                        onSnooze = { medication ->
                            medication?.let {
                                val scheduler = AlarmScheduler(this@OverlayService)
                                scheduler.snooze(it.id, it.name, it.dosage, 5)
                            }
                            stopSelf() 
                        }
                    )
                }
            }
        }

        val layoutParams = WindowManager.LayoutParams().apply {
            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams.TYPE_PHONE
            }
            format = PixelFormat.TRANSLUCENT
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            gravity = Gravity.TOP
            y = 100 // Padding from top
        }

        overlayView = composeView
        windowManager?.addView(overlayView, layoutParams)
    }

    @Composable
    fun OverlayContent(
        medicationId: Int,
        onDismiss: () -> Unit,
        onSnooze: (Medication?) -> Unit
    ) {
        val context = this
        var medication by remember { mutableStateOf<Medication?>(null) }

        LaunchedEffect(medicationId) {
            val repository = MedicationRepository(MedicationDatabase.getDatabase(context).medicationDao())
            medication = repository.getMedicationById(medicationId)
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "ئازیزم، کاتی دەرمانە!",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MedicalCyan,
                        fontSize = 22.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                val medName = medication?.name ?: "..."
                val dosage = medication?.dosage ?: "..."

                Text(
                    text = "ئازیزم، کاتی ئەوەیە دەرمانەکەت وەربگریت: $medName - $dosage. دڵنیابە کە هەموو شتێک باش دەبێت، تکایە ئاگاداری خۆت بە.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { onSnooze(medication) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = GlassBackground),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("دواخستن بۆ ٥ خولەک", color = TextSecondary, fontSize = 12.sp)
                    }

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MedicalCyan),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("بینیم", color = SurfaceDark, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleRegistry?.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        lifecycleRegistry?.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        overlayView?.let { windowManager?.removeView(it) }
        serviceScope.cancel()
    }
}
