package com.example.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import com.example.data.AlarmScheduler
import com.example.data.MedicationDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MedicationAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let { ctx ->
            if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
                // Re-schedule all active medications after boot
                val database = MedicationDatabase.getDatabase(ctx)
                val scheduler = AlarmScheduler(ctx)
                CoroutineScope(Dispatchers.IO).launch {
                    val activeMedications = database.medicationDao().getAllMedicationsSync().filter { it.isActive }
                    activeMedications.forEach { medication ->
                        scheduler.schedule(medication)
                    }
                }
            } else {
                val medicationId = intent?.getIntExtra("EXTRA_MEDICATION_ID", -1) ?: -1
                val medicationName = intent?.getStringExtra("EXTRA_MEDICATION_NAME") ?: "دەرمان"
                val medicationDosage = intent?.getStringExtra("EXTRA_MEDICATION_DOSAGE") ?: ""

                showNotification(ctx, medicationId, medicationName, medicationDosage)
                
                // Start Overlay Service if permission is granted
                if (com.example.util.PermissionUtils.hasOverlayPermission(ctx)) {
                    val overlayIntent = Intent(ctx, com.example.service.OverlayService::class.java).apply {
                        putExtra("medication_id", medicationId)
                    }
                    ctx.startService(overlayIntent)
                }
            }
        }
    }

    private fun showNotification(context: Context, id: Int, name: String, dosage: String) {
        val channelId = "medication_reminders"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        val sharedPreferences = context.getSharedPreferences("darman_prefs", Context.MODE_PRIVATE)
        val ringtoneUriString = sharedPreferences.getString("ringtone_uri", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString())
        val ringtoneUri = Uri.parse(ringtoneUriString)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Delete existing channel to update sound if necessary (simplest way to update sound on existing channel)
            // Or just create it, but once created, sound might be immutable for some Android versions.
            // Actually, for consistency, we might want to use a different channel ID if sound changes, 
            // but let's try just setting it.
            val channel = NotificationChannel(
                channelId,
                "بیرخەرەوەی دەرمان",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "ئاگادارکەرەوە بۆ وەرگرتنی دەرمانەکان لە کاتی خۆیدا"
                setSound(ringtoneUri, android.media.AudioAttributes.Builder()
                    .setUsage(android.media.AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build())
            }
            notificationManager.createNotificationChannel(channel)
        }

        val activityIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            id,
            activityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm) // Use a better icon if available
            .setContentTitle("کاتی وەرگرتنی دەرمان: $name")
            .setContentText("بڕی دەرمان: $dosage")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(id, notification)
    }
}
