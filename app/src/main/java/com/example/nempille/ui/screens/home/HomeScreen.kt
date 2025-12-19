package com.example.nempille.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.nempille.domain.model.UserRole
import com.example.nempille.ui.auth.AuthViewModel
import com.example.nempille.ui.navigation.Screen

//home screen shown after LOgin
//nav demo - added buttons to go to other screens
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    //only redirect when auth state is initialized AND user is not logged in
    LaunchedEffect(uiState.isAuthInitialized, uiState.isLoggedIn) {
        if (uiState.isAuthInitialized && !uiState.isLoggedIn) {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Home.route) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp) // Increased space between buttons
    ) {

        Text(
            text = "Welcome, ${uiState.currentUser?.name ?: ""}",
            style = MaterialTheme.typography.headlineLarge, // Bigger welcome text
            modifier = Modifier.padding(vertical = 24.dp)
        )

        // Different UI based on role
        when (uiState.currentUser?.role) {
            UserRole.PATIENT -> {
                DashboardButton(
                    text = "Your Medications",
                    icon = Icons.Filled.MedicalServices,
                    onClick = { navController.navigate(Screen.MedicationList.route) }
                )
                DashboardButton(
                    text = "History",
                    icon = Icons.Filled.History,
                    onClick = { navController.navigate(Screen.History.route) }
                )
            }

            UserRole.CAREGIVER -> {
                DashboardButton(
                    text = "Your Patients",
                    icon = Icons.Filled.People,
                    onClick = { navController.navigate(Screen.Caregiver.route) }
                )
            }

            null -> {}
        }

        DashboardButton(
            text = "Notifications",
            icon = Icons.Filled.Notifications,
            onClick = { navController.navigate(Screen.Notifications.route) }
        )

        DashboardButton(
            text = "Settings",
            icon = Icons.Filled.Settings,
            onClick = { navController.navigate(Screen.Settings.route) }
        )

        Spacer(modifier = Modifier.weight(1f)) // Pushes logout to the bottom

        DashboardButton(
            text = "Log out",
            icon = Icons.Filled.Logout,
            onClick = { viewModel.logout() }
        )
    }
}

@Composable
private fun DashboardButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start // Align content to the start
        ) {
            Spacer(modifier = Modifier.size(16.dp))
            Icon(
                imageVector = icon,
                contentDescription = null, // Text on button is descriptive enough
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.size(24.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}
