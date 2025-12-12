package com.example.nempille.ui.screens.setup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.nempille.ui.navigation.Screen

@Composable
fun SetupNotificationsScreen(
    navController: NavController,
    viewModel: SetupViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Local state for this screen's UI
    var wantsToNotify by remember { mutableStateOf(uiState.wantsToNotifyCaregiver) }
    var caregiverMobile by remember { mutableStateOf(uiState.caregiverMobile) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Notifications & Alerts", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(32.dp))

        // This is a placeholder for future alert settings
        Text("Alert settings (like choosing a sound or vibration pattern) can be configured later in the main app settings.",
            style = MaterialTheme.typography.bodyLarge
        )

        Divider(Modifier.padding(vertical = 32.dp))

        // Caregiver notification section
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Notify a caregiver?", style = MaterialTheme.typography.titleLarge)
            Switch(
                checked = wantsToNotify,
                onCheckedChange = { wantsToNotify = it }
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            "If you miss a dose, we can send an SMS alert to a family member or caregiver.",
            style = MaterialTheme.typography.bodyMedium
        )

        // Show the phone number field only if the switch is on
        if (wantsToNotify) {
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = caregiverMobile,
                onValueChange = { caregiverMobile = it },
                label = { Text("Caregiver's Mobile Number") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
        }

        Spacer(Modifier.weight(1f)) // Pushes the button to the bottom

        Button(
            onClick = {
                viewModel.onCaregiverInfoChanged(wantsToNotify, caregiverMobile)
                navController.navigate(Screen.SetupSummary.route)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Next: Review Summary")
        }
    }
}
