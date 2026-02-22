package com.example.nempille.ui.screens.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.nempille.notifications.MedicationReminderReceiver
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

/**
 * CONCEPT: AlarmManager & Scheduling
 * 
 * How it works:
 * This class uses AlarmManager, the system service for scheduling 
 * time-based tasks. It creates a PendingIntent that targets the 
 * MedicationReminderReceiver and schedules it to run at a specific time, 
 * even if the app is in the background or the phone is in Doze mode.
 */
@Singleton
class MedicationReminderScheduler @Inject constructor(
    private val context: Context
) {

    fun scheduleDailyReminder(
        requestId: Int,
        medicationName: String,
        dosage: String?,
        hour: Int,
        minute: Int
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, MedicationReminderReceiver::class.java).apply {
            putExtra(MedicationReminderReceiver.Companion.EXTRA_MEDICATION_ID, requestId)
            putExtra(MedicationReminderReceiver.Companion.EXTRA_MEDICATION_NAME, medicationName)
            putExtra(MedicationReminderReceiver.Companion.EXTRA_MEDICATION_DOSAGE, dosage)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        /**
         * CONCEPT: AlarmManager - setRepeating
         * 
         * How it works:
         * This schedules a repeating alarm. 
         * - RTC_WAKEUP: Wakes the device if it's asleep.
         * - calendar.timeInMillis: The first trigger time.
         * - AlarmManager.INTERVAL_DAY: Repeats the alarm every 24 hours.
         */
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun cancelReminder(requestId: Int) {
        val intent = Intent(context, MedicationReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}
