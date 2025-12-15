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
fun SetupAgeAndPillCountScreen(
    navController: NavController,
    viewModel: SetupViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var age by remember { mutableStateOf(uiState.age) }
    var pillCount by remember { mutableStateOf(uiState.pillCount) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Tell us about yourself", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("How old are you?") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = pillCount,
            onValueChange = { pillCount = it },
            label = { Text("How many different medications?") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(32.dp))
        Button(
            onClick = {
                viewModel.onAgeAndPillCountChanged(age, pillCount)
                viewModel.prepareMedicationList()
                navController.navigate(Screen.SetupMedicationNames.route)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = age.isNotBlank() && pillCount.isNotBlank() && (pillCount.toIntOrNull() ?: 0) > 0
        ) {
            Text("Next")
        }
    }
}
