package com.example.nempille.ui.navigation

//sealed clas, defines ALL destinations (screens) of app
//each object WITH unique "route" string used by Navigation

//why sealed? closed family types->compiler knows all cases
//no wrong ROUTE use

//new screen -> new object here

sealed class Screen (val route: String){
    //Login screen (first after app starts)
    data object Login : Screen("login")
    //Sign up - registration
    data object Signup : Screen("signup")
    //Home screen (after login)
    data object Home : Screen("home")
    //Medications for the current user
    data object MedicationList : Screen("medication_list")
    //Caregiver view (list of patients, for instance)
    data object Caregiver : Screen("caregiver")
    //App settings
    data object Settings : Screen ("settings")
    //Notifications
    data object Notifications : Screen("notifications")
}