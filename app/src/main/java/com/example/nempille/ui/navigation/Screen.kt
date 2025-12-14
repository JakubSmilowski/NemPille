package com.example.nempille.ui.navigation

//sealed clas, defines ALL destinations (screens) of app
//each object WITH unique "route" string used by Navigation

//why sealed? closed family types->compiler knows all cases
//no wrong ROUTE use

//new screen -> new object here

sealed class Screen (val route: String){
    //splash - first screen that decides where to go (login or home)
    data object Splash : Screen("splash")

    //Login screen (after splash if not logged in)
    data object Login : Screen("login")

    //Sign up - registration
    data object Signup : Screen("signup")

    //Home screen (after login)
    data object Home : Screen("home")

    //Medications for the current user
    data object MedicationList : Screen("medication_list")

    //Screen to add medication
    data object AddMedication : Screen("add_medication")

    // EDIT MEDICATION with navigation argument
    data object EditMedication : Screen("edit_medication/{medicationId}") {

        // Helper to build a concrete route, e.g. "edit_medication/5"
        fun createRoute(medicationId: Int): String =
            "edit_medication/$medicationId"
    }

    // Screen to show a given patient's meds
    data object PatientMedications : Screen("patient_medications/{patientId}") {

        // Helper to build actual path from a patient id
        fun createRoute(patientId: Int): String = "patient_medications/$patientId"
    }

    //caregiver screen
    data object Caregiver : Screen("caregiver")

    // Screen where caregiver can create a new patient and link to themselves
    data object AddPatient : Screen("add_patient")

    //App settings
    data object Settings : Screen ("settings")

    //Notifications
    data object Notifications : Screen("notifications")
}