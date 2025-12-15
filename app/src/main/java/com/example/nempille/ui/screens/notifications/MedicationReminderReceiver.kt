package com.example.nempille.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.nempille.ui.screens.notifications.NotificationHelper

//BroadcastReceiver triggered by AlarmManager to show medication reminder

//when AlarmManager triggers,Android sends an Intent to MedicationReminderReceiver
//this receiver reads the data from the Intent and calls NotificationHelper

//receiver is the 'bridge' between time-based alarms and UI visible notifications
//decoupled from UI, works even if the app is in background

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
