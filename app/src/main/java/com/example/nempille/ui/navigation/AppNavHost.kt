package com.example.nempille.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.nempille.ui.screens.caregiver.CaregiverScreen
import com.example.nempille.ui.screens.home.HomeScreen
import com.example.nempille.ui.screens.login.LoginScreen
import com.example.nempille.ui.screens.medication.MedicationListScreen
import com.example.nempille.ui.screens.notifications.NotificationsScreen
import com.example.nempille.ui.screens.settings.SettingsScreen
import com.example.nempille.ui.screens.signup.SignupScreen

//composable HOSTS - entire navigation graph
//tells Navigation which composable belongs to which route

@Composable
fun AppNavHost(
    navController : NavHostController
){
    //NavHost connects NavController with navigation graph
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route //1st screen when app launches
    ) {
        //each 'composable' defines 1 destination in graph
        composable (route =  Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable (route = Screen.Signup.route) {
            SignupScreen(navController = navController)
        }
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(route = Screen.MedicationList.route) {
            MedicationListScreen(navController = navController)
        }
        composable (route = Screen.Caregiver.route) {
            CaregiverScreen(navController = navController)
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
        composable(route = Screen.Notifications.route) {
            NotificationsScreen(navController = navController)
        }
        composable(route = Screen.Caregiver.route) {
            CaregiverScreen(navController = navController)
        }
    }
}
//NavHost shows composables
//startDestination - shows first route