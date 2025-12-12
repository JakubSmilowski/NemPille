package com.example.nempille.ui.screens.setup

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.nempille.domain.model.UserRole
import com.example.nempille.ui.navigation.Screen

@Composable
fun WelcomeScreen(
    navController: NavController,
    viewModel: SetupViewModel = hiltViewModel()
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to NemPille",
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "To get started, please tell us who you are.",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(64.dp))

            // Patient Button
            RoleButton(
                text = "I am a Patient",
                onClick = {
                    viewModel.onRoleSelected(UserRole.PATIENT)
                    navController.navigate(Screen.SetupProfile.route)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Caregiver Button
            RoleButton(
                text = "I am a Caregiver",
                onClick = {
                    navController.navigate(Screen.Signup.route)
                }
            )
        }
    }
}

@Composable
private fun RoleButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Text(text, style = MaterialTheme.typography.titleLarge)
    }
}
