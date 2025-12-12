package com.example.nempille.ui.screens.setup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.nempille.ui.navigation.Screen

@Composable
fun SetupProfileScreen(
    navController: NavController,
    viewModel: SetupViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Local state to hold text field values to avoid recomposing on every key press
    var name by remember { mutableStateOf(uiState.name) }
    var email by remember { mutableStateOf(uiState.email) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Let's create your profile", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Your Full Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Your Email Address") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            )
        )
        Spacer(Modifier.height(32.dp))

        Button(
            onClick = {
                // Update the ViewModel with the final values and navigate
                viewModel.onProfileInfoChanged(name, email)
                navController.navigate(Screen.SetupAgeAndPillCount.route)
            },
            modifier = Modifier.fillMaxWidth(),
            // Enable the button only when both fields are not blank
            enabled = name.isNotBlank() && email.isNotBlank()
        ) {
            Text("Next")
        }
    }
}
