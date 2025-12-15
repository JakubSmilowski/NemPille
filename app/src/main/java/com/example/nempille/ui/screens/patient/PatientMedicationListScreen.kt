package com.example.nempille.ui.screens.patient

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.nempille.domain.model.Medication

// Screen that shows all medications for a given patient
//@param patientId is mainly for clarity / debugging
//The ViewModel also reads it from SavedStateHandle
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientMedicationListScreen(
    navController: NavController,
    patientId: Int,
    viewModel: PatientMedicationListViewModel = hiltViewModel()
) {
    // Collect the list of medications from ViewModel
    val medications by viewModel.medications.collectAsState()

    // Scaffold gives us top bar + content slots
    Scaffold(
        topBar = {
            androidx.compose.material3.TopAppBar(
                title = { Text("Patient Medications") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            // Optional: show which patientId we’re looking at (for debugging/demo)
            Text(
                text = "Patient ID: $patientId",
                style = MaterialTheme.typography.bodySmall
            )

            // If list is empty, show a friendly message
            if (medications.isEmpty()) {
                Text(
                    text = "This patient has no medications yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 16.dp)
                )
            } else {
                // List of medications
                LazyColumn(
                    contentPadding = PaddingValues(top = 16.dp)
                ) {
                    items(medications) { med ->
                        MedicationItem(medication = med)
                    }
                }
            }
        }
    }
}

//Simple list item for a medication.
//Uses fields from Medication domain model:
//name, dosage, frequencyPerDay, notes
@Composable
private fun MedicationItem(
    medication: Medication
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = medication.name,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Dosage: ${medication.dosage}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Times per day: ${medication.frequencyPerDay}",
            style = MaterialTheme.typography.bodySmall
        )

        // Show notes only if not null/blank
        if (!medication.notes.isNullOrBlank()) {
            Text(
                text = "Notes: ${medication.notes}",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Divider(modifier = Modifier.padding(top = 8.dp))
    }
}