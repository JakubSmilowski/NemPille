package com.example.nempille.ui.screens.setup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.nempille.ui.navigation.Screen

@Composable
fun SetupMedicationNamesScreen(
    navController: NavController,
    viewModel: SetupViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("What are your medications?", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(24.dp))

        // This LazyColumn now directly uses the list from the ViewModel's state
        LazyColumn(modifier = Modifier.weight(1f)) {
            itemsIndexed(
                items = uiState.medications,
                // Add a key for performance and stability
                key = { index, _ -> index }
            ) { index, medication ->
                OutlinedTextField(
                    value = medication.name,
                    // When the text changes, call the ViewModel function directly
                    onValueChange = { newName ->
                        viewModel.onMedicationNameChanged(index, newName)
                    },
                    label = { Text("Medication ${index + 1} Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    singleLine = true
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                // No need to update the ViewModel here, it's already up-to-date!
                // Just navigate to the schedule screen for the FIRST medication.
                navController.navigate(Screen.SetupMedicationSchedule.createRoute(0))
            },
            modifier = Modifier.fillMaxWidth(),
            // The button is enabled only when all medication names have been entered.
            enabled = uiState.medications.all { it.name.isNotBlank() }
        ) {
            Text("Next: Set Up First Medication")
        }
    }
}
