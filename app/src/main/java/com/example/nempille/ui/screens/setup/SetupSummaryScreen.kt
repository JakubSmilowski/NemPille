package com.example.nempille.ui.screens.setup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.nempille.ui.navigation.Screen
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun SetupSummaryScreen(
    navController: NavController,
    viewModel: SetupViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // This effect listens for when the setup is finished in the ViewModel.
    // When it's true, we navigate to Home and clear the entire setup back stack.
    LaunchedEffect(uiState.setupFinished) {
        if (uiState.setupFinished) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.WelcomeScreen.route) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Please confirm your setup", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            // Profile Summary
            item {
                SummaryCard(title = "My Profile") {
                    SummaryRow("Name", uiState.name)
                    SummaryRow("Email", uiState.email)
                    SummaryRow("Age", uiState.age)
                }
            }

            // Medication Summary
            item {
                Spacer(Modifier.height(16.dp))
                Text("Medications", style = MaterialTheme.typography.titleLarge)
            }
            items(uiState.medications) { med ->
                MedicationSummaryCard(med)
            }

            // Caregiver Summary
            item {
                if (uiState.wantsToNotifyCaregiver) {
                    SummaryCard(title = "Caregiver Alerts") {
                        SummaryRow("Notify Caregiver", "Yes")
                        SummaryRow("Mobile Number", uiState.caregiverMobile)
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { viewModel.completeSetup() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Confirm & Finish")
        }
    }
}

@Composable
private fun SummaryCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Divider(Modifier.padding(vertical = 8.dp))
            content()
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun MedicationSummaryCard(med: MedicationSetupState) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(med.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            val frequencyText = if (med.isDaily) "Daily" else med.weeklyFrequency.joinToString(", ")
            SummaryRow("Frequency", frequencyText)

            med.schedules.forEach { schedule ->
                val time = schedule.time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                SummaryRow("  - At $time", "${schedule.quantity} pill(s)")
            }

            if(med.additionalInfo.isNotBlank()) {
                SummaryRow("Notes", med.additionalInfo)
            }
        }
    }
}
