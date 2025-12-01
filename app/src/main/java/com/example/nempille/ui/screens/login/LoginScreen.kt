package com.example.nempille.ui.screens.login

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

//this composable represents the login screen UI
//now text and button 'go home'
@Composable
fun LoginScreen(
    navController: NavController //allows to navigate to other screens
) {
    Column(
        modifier = Modifier
            .fillMaxSize()          //all available screen space
            .padding(16.dp),   // outer padding
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Login Screen")

        //navigates to home when clicked
        Button(
            onClick = {
                navController.navigate(Screen.Home.route)
            }
        ) {
            Text(text = "Go home")
        }
    }
}