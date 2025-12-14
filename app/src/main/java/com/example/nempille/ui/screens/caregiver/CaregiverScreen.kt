package com.example.nempille.ui.screens.caregiver

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.nempille.ui.navigation.Screen

@Composable
fun CaregiverScreen(
    navController: NavController,
    viewModel: CaregiverViewModel = hiltViewModel()
) {
    // Observe current logged-in user
    val currentUser by viewModel.currentUser.collectAsState()
    // Observe list of patients assigned to this caregiver
    val patients by viewModel.patients.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {

        // Show who is logged in
        Text(
            text = "Caregiver: ${currentUser?.name ?: ""}",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Your Patients:",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = { navController.navigate(Screen.AddPatient.route) },
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        ) {
            Text("Add New Patient")
        }

        // List of patients
        LazyColumn {
            items(patients) { patient ->
                Card(
                    modifier = Modifier
                        .padding(vertical = 6.dp)
                        .fillMaxWidth()
                        .clickable {
                            // Navigate to medications for this patient
                            navController.navigate(
                                Screen.PatientMedications.createRoute(patient.id)
                            )
                        }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Patient name (domain model User.name)
                        Text(
                            text = patient.name,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
    }
}