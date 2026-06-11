package com.example.ui.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ui.components.GlassCard
import com.example.ui.components.MeshBackground
import com.example.util.PermissionUtils
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    val sharedPreferences = remember { context.getSharedPreferences("darman_prefs", Context.MODE_PRIVATE) }
    
    var ringtoneUri by remember { 
        mutableStateOf(sharedPreferences.getString("ringtone_uri", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString())) 
    }

    var isOverlayEnabled by remember {
        mutableStateOf(PermissionUtils.hasOverlayPermission(context))
    }
    
    val ringtoneName = remember(ringtoneUri) {
        try {
            val uri = Uri.parse(ringtoneUri)
            RingtoneManager.getRingtone(context, uri).getTitle(context)
        } catch (e: Exception) {
            "دیفالت"
        }
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri: Uri? = result.data?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
            uri?.let {
                ringtoneUri = it.toString()
                sharedPreferences.edit().putString("ringtone_uri", ringtoneUri).apply()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        MeshBackground()
        
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("ڕێکخستنەکان", style = MaterialTheme.typography.titleLarge, color = MedicalCyan) },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MedicalCyan)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            containerColor = Color.Transparent
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(20.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    "بەڕێوەبردنی ئاگادارکردنەوەکان",
                    style = MaterialTheme.typography.titleSmall,
                    color = MedicalPurple,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER).apply {
                            putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION)
                            putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "دیاریکردنی دەنگی ئاگادارکردنەوە")
                            putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Uri.parse(ringtoneUri))
                            putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
                            putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true)
                        }
                        launcher.launch(intent)
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                "دەنگی ئاگادارکردنەوە",
                                style = MaterialTheme.typography.titleMedium,
                                color = MedicalCyan
                            )
                            Text(
                                "دەنگی ئاگادارکردنەوەی دەرمانەکان لێرە بگۆڕە",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                textAlign = TextAlign.End
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "دەنگی چالاک: $ringtoneName",
                                style = MaterialTheme.typography.labelSmall,
                                color = MedicalPurple
                            )
                        }
                        Icon(
                            Icons.Default.NotificationsActive,
                            contentDescription = null,
                            tint = MedicalCyan,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (!isOverlayEnabled) {
                            PermissionUtils.requestOverlayPermission(context)
                        }
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.End, modifier = Modifier.weight(1f)) {
                            Text(
                                "پیشاندان لەسەر بەرنامەکانی تر",
                                style = MaterialTheme.typography.titleMedium,
                                color = MedicalPurple
                            )
                            Text(
                                "بۆ ئەوەی کاتی دەرمانەکە بە شێوەیەکی ڕونتر دەرکەوێت",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                textAlign = TextAlign.End
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Switch(
                            checked = isOverlayEnabled,
                            onCheckedChange = {
                                if (it && !isOverlayEnabled) {
                                    PermissionUtils.requestOverlayPermission(context)
                                }
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MedicalCyan,
                                checkedTrackColor = MedicalCyan.copy(alpha = 0.5f)
                            )
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    "تێبینی: گۆڕینی دەنگی زەنگ تەنها کاریگەری لەسەر ئەو ئاگادارکردنەوانە دەبێت کە لەمەودوا دادەنرێن.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
