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
 * Helper class that uses AlarmManager to schedule/cancel medication reminders.
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

        // Calculate the first trigger time: today at [hour:minute],
        // or tomorrow if that time has already passed.
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            //if chosen time is before 'now', schedule next day
            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        //repeating alarm every dy at that time
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        /*
        // DEBUG: fire in 10 seconds instead of using time:
        val triggerAt = System.currentTimeMillis() + 10_000L
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            triggerAt,
            pendingIntent
        )
        */
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