package com.example.nempille.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.nempille.domain.model.User
import com.example.nempille.ui.auth.AuthViewModel
import com.example.nempille.ui.common.collectAsStateWithLifecycleSafe
import com.example.nempille.ui.navigation.Screen
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

//observes AuthViewModel.uiState
//shows email field, errors, loading
//calls viwModel.login() on button click
//navigates home when login succeeds

//onLoginSuccess: callback to let NavHost / parent screen navigate to "home"
@Composable
fun LoginScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel()
) {
    //collect UI state from ViewModel
    val uiState by viewModel.uiState.collectAsStateWithLifecycleSafe()

    //react when user becomes logged in
    //when uiState.isLoggedIn TRUE and currentUser != null, navigate home

//    LaunchedEffect(uiState.isLoggedIn, uiState.currentUser) {
//        val user = uiState.currentUser
//        if (uiState.isLoggedIn && user != null) {
//            navController.navigate(Screen.Home.route) {
//                // Remove Login from back stack, so back button won't go back to Login
//                popUpTo(Screen.Login.route) { inclusive = true }
//            }
//        }
//    }

    //main layout container - centers content on screen
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        //column with vertical spacing between children
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineMedium
            )

            //email input field
            OutlinedTextField(
                value = uiState.email, //value from ViewModel
                onValueChange = viewModel::onEmailChanged, //tells ViewModel about changes
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email //show email keyboard
                ),
                modifier = Modifier.fillMaxWidth()
            )

            //password input field
            OutlinedTextField(
                value = uiState.password,
                onValueChange = viewModel::onPasswordChanged,
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            //show error message if there is no
            if (uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            ///login button
            Button(
                onClick = {
                    // We ask ViewModel to perform login and give it a callback
                    // that will run ONLY when login succeeds.
                    viewModel.login(
                        onSuccess = {
                            navController.navigate(Screen.Home.route) {
                                // Remove Login from back stack
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                    )
                },
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isLoading) {
                    //show small loader inside button while login is running
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(18.dp)
                            .padding(end = 8.dp),
                        strokeWidth = 2.dp
                    )
                    Text("Logging in…")
                } else {
                    Text("Log in")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            //navigate to Signup screen
            TextButton(
                onClick = { navController.navigate(Screen.Signup.route) }
            ) {
                Text("Don’t have an account? Sign up")
            }
            //in future can add:
            //role selection
            //forgot password, etc.
        }
    }
}