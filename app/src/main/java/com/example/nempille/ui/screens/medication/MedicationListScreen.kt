package com.example.nempille.ui.screens.medication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.nempille.domain.model.Medication

//shows list of medications from Room via ViewModel
@Composable
fun MedicationListScreen (
    navController: NavController,
    //hiltViewModel() asks Hilt to give instance of MedicationViewModel
    viewModel: MedicationViewModel = hiltViewModel()
){
    // Collect the StateFlow<List<Medication>> as a Compose State
    // Whenever the StateFlow changes, the UI recomposes automatically
    val medicationList by viewModel.medications.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top title
        Text(
            text = "Medication List",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button to add a sample medication to the database.
        Button(
            onClick = { viewModel.addSampleMedication() },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Add sample medication")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Show a message when the list is empty.
        if (medicationList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No medications yet. Tap the button to add one.")
            }
        } else {
            // List of medications using LazyColumn.
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                // items(...) iterates over the list and creates a row for each medication
                items(medicationList) { medication ->
                    MedicationItem(medication = medication)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

// Reusable composable to display a single medication item in a card
@Composable
fun MedicationItem(
    medication: Medication
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = medication.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = "Dosage: ${medication.dosage}")
            Text(text = "Times per day: ${medication.frequencyPerDay}")
            medication.notes?.let { notes ->
                Text(text = "Notes: $notes")
            }
        }
    }
}