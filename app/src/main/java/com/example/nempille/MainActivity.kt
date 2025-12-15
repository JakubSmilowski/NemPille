package com.example.nempille

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.nempille.ui.navigation.AppNavHost
import com.example.nempille.ui.screens.notifications.NotificationHelper
import com.example.nempille.ui.theme.NemPilleTheme
import dagger.hilt.android.AndroidEntryPoint

//main entry point
@AndroidEntryPoint //tells 'u can inject dependencies in this Activity'
class MainActivity : ComponentActivity() {
    // Launcher for POST_NOTIFICATIONS permission (Android 13+)
    private val requestNotificationPermission =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            // You could show a Toast if needed:
            // if (!isGranted) { Toast.makeText(this, "Notifications disabled", Toast.LENGTH_SHORT).show() }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1) Create notification channel once at startup
        NotificationHelper.createNotificationChannel(this)

        // 2) Ask for notification permission on Android 13+ if not granted
        askNotificationPermissionIfNeeded()

        enableEdgeToEdge()

        setContent {
            NemPilleTheme {
                val navController = rememberNavController()

                // Surface gives a background using the current Material theme.
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    // NavHost with all the destinations (screens)
                    AppNavHost(navController = navController)
                }
            }
        }
    }

    /**
     * On Android 13+ (API 33), apps must ask the user for POST_NOTIFICATIONS permission.
     * For lower versions, this permission does not exist, so we do nothing.
     */
    private fun askNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            val isGranted = ContextCompat.checkSelfPermission(this, permission) ==
                    PackageManager.PERMISSION_GRANTED

            if (!isGranted) {
                // Trigger the permission dialog
                requestNotificationPermission.launch(permission)
            }
        }
    }
}

// The preview + Greeting composable are just template demo code
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NemPilleTheme {
        Greeting("Android")
    }
}