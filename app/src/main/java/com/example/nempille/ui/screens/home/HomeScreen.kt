package com.example.nempille.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nempille.ui.navigation.Screen

//home screen shown after LOgin
//nav demo - added buttons to go to other screens
@Composable
fun HomeScreen (
    navController: NavController

) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Home Screen")

        Button(onClick = { navController.navigate(Screen.MedicationList.route) }) {
            Text(text = "Go to Medication List")
        }

        Button(onClick = { navController.navigate(Screen.Caregiver.route) }) {
            Text(text = "Go to Caregiver Screen")
        }

        Button(onClick = { navController.navigate(Screen.Settings.route) }) {
            Text(text = "Go to Settings")
        }

        Button(onClick = { navController.navigate(Screen.Notifications.route) }) {
            Text(text = "Go to Notifications")
        }
    }
}