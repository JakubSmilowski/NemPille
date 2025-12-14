package com.example.nempille.ui.screens.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nempille.ui.screens.notifications.NotificationHelper

/**
 * Simple screen for now that lets you send a test notification
 * to check that channels + permissions + NotificationHelper work.
 */
@Composable
fun NotificationsScreen(
    navController: NavController
) {
    // We need a Context to call NotificationHelper
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Notifications",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Tap the button below to send a test notification.",
                style = MaterialTheme.typography.bodyMedium
            )

            Button(
                onClick = {
                    // Directly show a notification without AlarmManager.
                    NotificationHelper.showMedicationReminder(
                        context = context,
                        notificationId = 999,  // any integer ID
                        medicationName = "Test pill",
                        dosage = "Just testing!"
                    )
                }
            ) {
                Text("Send test notification")
            }
        }
    }
}