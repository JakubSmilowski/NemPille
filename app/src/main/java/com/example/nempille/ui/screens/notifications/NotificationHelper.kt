package com.example.nempille.ui.screens.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.nempille.R

//Helper object to handle notification channel creation and
//building/showing medication reminder notifications.

//receiver is the ;bridge; between time-based alarms and UI notifications
//decoupled from UI, so can work if the app is in the background

object NotificationHelper {

    // Unique ID for this notification channel
    const val CHANNEL_ID = "medication_reminders_channel"

    //Create the notification channel if needed (Android 8+).
    //Should be called once, e.g. at app startup.
    fun createNotificationChannel(context: Context) {
        // Notification channels are required only from API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Medication reminders"
            val descriptionText = "Notifications reminding you to take your medication"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Show a single medication reminder notification
     *
     * @param notificationId Unique ID per reminder so notifications don't override each other
     * @param medicationName Name of the medication
     * @param dosage (optional) dosage text to show in the notification body
     */
    fun showMedicationReminder(
        context: Context,
        notificationId: Int,
        medicationName: String,
        dosage: String? = null
    ) {
        // Build the notification content text
        val contentText = if (!dosage.isNullOrBlank()) {
            "Time to take $medicationName ($dosage)"
        } else {
            "Time to take $medicationName"
        }

        // Build a notification using NotificationCompat

        //every notification MUST have a small icon as Android requirement
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_pill) // make sure this icon exists in res/drawable
            .setContentTitle("Medication Reminder")
            .setContentText(contentText)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build())
        }
    }
}