package com.example.nempille.ui.navigation

import android.window.SplashScreen
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
import com.example.nempille.ui.splash.SplashScreen
import com.example.nempille.ui.screens.medication.AddMedicationScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.nempille.ui.screens.medication.EditMedicationScreen
import com.example.nempille.ui.screens.patient.PatientMedicationListScreen
import com.example.nempille.ui.screens.patient.AddPatientScreen

//composable HOSTS - entire navigation graph
//tells Navigation which composable belongs to which route

@Composable
fun AppNavHost(
    navController : NavHostController
){
    //NavHost connects NavController with navigation graph
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route //1st screen when app launches
    ) {
        //SPLASH
        composable(route = Screen.Splash.route) {
            SplashScreen(navController = navController)
        }

        //LOGIN
        composable (route = Screen.Login.route) {
            LoginScreen(navController = navController)
        }

        //SIGNUP
        composable (route = Screen.Signup.route) {
            SignupScreen(navController = navController)
        }

        //HOME
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        //MEDICATION LIST
        composable(route = Screen.MedicationList.route) {
            MedicationListScreen(navController = navController)
        }

        //ADD MEDICATION
        composable(route = Screen.AddMedication.route) {
            AddMedicationScreen(navController = navController)
        }

        // EDIT MEDICATION
        composable(
            route = Screen.EditMedication.route,
            arguments = listOf(
                navArgument("medicationId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val medicationId = backStackEntry.arguments?.getInt("medicationId") ?: 0
            EditMedicationScreen(
                navController = navController,
                medicationId = medicationId
            )
        }

        //CAREGIVER
        composable (route = Screen.Caregiver.route) {
            CaregiverScreen(navController = navController)
        }

        // ADD PATIENT (caregiver creates new patient)
        composable(route = Screen.AddPatient.route) {
            AddPatientScreen(navController = navController)
        }

        //PATIENT MEDICATION (takes patientId as argument)
        composable(
            route = Screen.PatientMedications.route,
            arguments = listOf(
                navArgument("patientId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            // Read the patientId from navigation arguments
            val patientId = backStackEntry.arguments?.getInt("patientId") ?: 0

            PatientMedicationListScreen(
                navController = navController,
                patientId = patientId
            )
        }

        //SETTINGS
        composable(route = Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }

        //NOTIFICATIONS
        composable(route = Screen.Notifications.route) {
            NotificationsScreen(navController = navController)
        }
    }
}
//NavHost shows composables
//startDestination - shows first route