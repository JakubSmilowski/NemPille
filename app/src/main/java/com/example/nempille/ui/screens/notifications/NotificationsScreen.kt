package com.example.nempille.ui.screens.notifications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Test Notifications",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        NotificationButton(
            text = "Ibuprofen - Compartment 2",
            onClick = {
                NotificationHelper.showTestMedicationReminder(
                    context = context,
                    notificationId = 999,
                    medicationName = "Ibuprofen",
                    note = "Take_with_food",
                    motor = 0
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        NotificationButton(
            text = "Creatine - Compartment 1",
            onClick = {
                NotificationHelper.showTestMedicationReminder(
                    context = context,
                    notificationId = 1000,
                    medicationName = "Creatine",
                    note = "after_food",
                    motor = 1
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        NotificationButton(
            text = "Vitamin D - Compartment 4",
            onClick = {
                NotificationHelper.showTestMedicationReminder(
                    context = context,
                    notificationId = 1001,
                    medicationName = "Vitamin D",
                    note = "with_water",
                    motor = 0
                )
            }
        )
    }
}

@Composable
private fun NotificationButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Text(text, style = MaterialTheme.typography.headlineSmall)
    }
}
