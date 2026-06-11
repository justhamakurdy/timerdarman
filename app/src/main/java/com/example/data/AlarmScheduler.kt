package com.example.data

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.receiver.MedicationAlarmReceiver
import java.util.*

class AlarmScheduler(private val context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun schedule(medication: Medication) {
        val intent = Intent(context, MedicationAlarmReceiver::class.java).apply {
            putExtra("EXTRA_MEDICATION_ID", medication.id)
            putExtra("EXTRA_MEDICATION_NAME", medication.name)
            putExtra("EXTRA_MEDICATION_DOSAGE", medication.dosage)
        }

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, medication.alarmHour)
            set(Calendar.MINUTE, medication.alarmMinute)
            set(Calendar.SECOND, 0)
            
            // If the time is in the past, schedule for tomorrow
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            PendingIntent.getBroadcast(
                context,
                medication.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    fun snooze(medicationId: Int, medicationName: String, medicationDosage: String, minutes: Int = 5) {
        val intent = Intent(context, MedicationAlarmReceiver::class.java).apply {
            putExtra("EXTRA_MEDICATION_ID", medicationId)
            putExtra("EXTRA_MEDICATION_NAME", medicationName)
            putExtra("EXTRA_MEDICATION_DOSAGE", medicationDosage)
        }

        val triggerTime = System.currentTimeMillis() + (minutes * 60 * 1000)

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            PendingIntent.getBroadcast(
                context,
                medicationId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    fun cancel(medication: Medication) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                medication.id,
                Intent(context, MedicationAlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}
