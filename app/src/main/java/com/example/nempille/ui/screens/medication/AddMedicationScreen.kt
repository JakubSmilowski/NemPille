package com.example.nempille.ui.screens.medication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

// Screen that lets the current logged-in user create a new medication.
// For now, we use local Compose state for the form fields and then call
// MedicationViewModel.addMedication().

@Composable
fun AddMedicationScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: MedicationViewModel = hiltViewModel()
) {
    // Local form state
    var name by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var frequencyText by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    // Simple local error text for validation
    var errorText by remember { mutableStateOf<String?>(null) }

    // Local loading flag (for button state)
    var isSaving by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Add Medication",
                style = MaterialTheme.typography.headlineMedium
            )

            // Name (required)
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    errorText = null
                },
                label = { Text("Name (e.g. Ibuprofen)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Dosage (required, free text)
            OutlinedTextField(
                value = dosage,
                onValueChange = {
                    dosage = it
                    errorText = null
                },
                label = { Text("Dosage (e.g. 1 pill, 10mg)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Frequency per day (integer)
            OutlinedTextField(
                value = frequencyText,
                onValueChange = {
                    frequencyText = it
                    errorText = null
                },
                label = { Text("Times per day") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Notes (optional)
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes (optional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 80.dp)
            )

            // Error message if validation fails
            if (errorText != null) {
                Text(
                    text = errorText ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // SAVE button
            Button(
                onClick = {
                    // Basic validation
                    if (name.isBlank() || dosage.isBlank() || frequencyText.isBlank()) {
                        errorText = "Name, dosage and times per day are required"
                        return@Button
                    }

                    val freq = frequencyText.toIntOrNull()
                    if (freq == null || freq <= 0) {
                        errorText = "Times per day must be a positive number"
                        return@Button
                    }

                    isSaving = true

                    // Ask ViewModel to save the medication for current user
                    viewModel.addMedication(
                        name = name.trim(),
                        dosage = dosage.trim(),
                        frequencyPerDay = freq,
                        notes = notes.ifBlank { null }
                    )

                    // For now we optimistically navigate back.
                    // Later you could listen for success state.
                    navController.popBackStack()
                },
                enabled = !isSaving,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(18.dp)
                            .padding(end = 8.dp),
                        strokeWidth = 2.dp
                    )
                    Text("Saving…")
                } else {
                    Text("Save")
                }
            }

            // CANCEL button
            TextButton(
                onClick = { navController.popBackStack() }
            ) {
                Text("Cancel")
            }
        }
    }
}