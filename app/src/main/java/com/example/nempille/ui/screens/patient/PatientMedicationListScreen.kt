package com.example.nempille.ui.screens.patient

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun PatientMedicationListScreen(
    navController: NavController,
    patientId: Int,
    viewModel: PatientMedicationListViewModel = hiltViewModel()
) {
    val medications by viewModel.medications.collectAsState()

    Column(Modifier.padding(16.dp)) {
        Text("Patient Medications", style = MaterialTheme.typography.titleLarge)

        LazyColumn {
            items(medications) { med ->
                Text("${med.name} — ${med.dosage}")
            }
        }
    }
}
