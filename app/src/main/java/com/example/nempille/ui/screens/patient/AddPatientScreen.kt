package com.example.nempille.ui.screens.patient

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun AddPatientScreen(
    navController: NavController,
    viewModel: AddPatientViewModel = hiltViewModel()
) {
    val patientName by viewModel.patientName.collectAsState()
    val relation by viewModel.relationToPatient.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = "Add New Patient",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = patientName,
            onValueChange = { viewModel.patientName.value = it },
            label = { Text("Patient name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = relation,
            onValueChange = { viewModel.relationToPatient.value = it },
            label = { Text("Relation (e.g. mother, client)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.onSaveClicked(
                    onSuccess = {
                        // Navigate back to caregiver screen after success
                        navController.popBackStack()
                    },
                    onError = { message ->
                        // Show error in snackbar
                        // Launch snackbar in a coroutine
                        // must be inside LaunchedEffect or rememberCoroutineScope
                        // -> simplest: use rememberCoroutineScope
                    }
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save patient")
        }

        // (Optional) Snackbar host for showing errors
        SnackbarHost(hostState = snackbarHostState)
    }

    // Handle showing snackbar error messages outside of Column
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        // nothing yet, but we could pass a callback that uses snackbarHostState
    }
}
