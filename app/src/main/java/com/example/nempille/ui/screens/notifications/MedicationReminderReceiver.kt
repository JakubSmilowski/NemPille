package com.example.nempille.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.nempille.ui.screens.notifications.NotificationHelper

/**
 * CONCEPT: BroadcastReceiver
 * 
 * How it works:
 * This class listens for system-wide broadcasts. When the AlarmManager 
 * triggers an alarm, it sends an Intent that this receiver is 
 * registered to handle (see AndroidManifest.xml). The `onReceive` 
 * method is then executed, which in turn calls the NotificationHelper 
 * to display the reminder.
 */
class MedicationReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val medicationId = intent.getIntExtra(EXTRA_MEDICATION_ID, 0)
        val medicationName = intent.getStringExtra(EXTRA_MEDICATION_NAME) ?: "Medication"
        val dosage = intent.getStringExtra(EXTRA_MEDICATION_DOSAGE)

        NotificationHelper.showMedicationReminder(
            context = context,
            notificationId = medicationId,
            medicationName = medicationName,
            dosage = dosage
        )
    }

    companion object {
        const val EXTRA_MEDICATION_ID = "extra_medication_id"
        const val EXTRA_MEDICATION_NAME = "extra_medication_name"
        const val EXTRA_MEDICATION_DOSAGE = "extra_medication_dosage"
    }
}
