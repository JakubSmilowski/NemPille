package com.example.nempille.ui.screens.settings

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice

import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.nempille.domain.model.User

@SuppressLint("MissingPermission")
@Composable
fun SettingsScreen(
    navController: NavController,
    bluetoothViewModel: BluetoothViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val bluetoothUiState by bluetoothViewModel.uiState.collectAsState()
    val userState by settingsViewModel.userState.collectAsState()
    val context = LocalContext.current

    val permissionsToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        arrayOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT
        )
    } else {
        arrayOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val allGranted = perms.values.all { it }
        if (allGranted) {
            bluetoothViewModel.scanForDevices()
        } else {
            Toast.makeText(context, "Bluetooth permission is needed to scan!", Toast.LENGTH_LONG).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "My Profile",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (userState.isLoading) {
            CircularProgressIndicator()
        } else {
            userState.currentUser?.let { user ->
                UserCard(user = user)
            } ?: Text("User not found.")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Divider()
        Spacer(modifier = Modifier.height(24.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Find device in your range:",
                style = MaterialTheme.typography.headlineSmall
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Scan")
                Spacer(modifier = Modifier.width(8.dp))

                Switch(
                    checked = bluetoothUiState.isScanning,
                    onCheckedChange = { shouldScan ->
                        if (shouldScan) {
                            permissionLauncher.launch(permissionsToRequest)
                        } else {
                            bluetoothViewModel.scanForDevices()
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (bluetoothUiState.isScanning) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
        }

        Text(
            text = "Available Devices:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(bluetoothUiState.scannedDevices) { device ->
                BluetoothDeviceItem(
                    device = device,
                    onClick = {
                        permissionLauncher.launch(permissionsToRequest)
                        bluetoothViewModel.connectToDevice(device)
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        StatusCard(bluetoothUiState.connectedDevice, bluetoothUiState.errorMessage)
    }
}

@SuppressLint("MissingPermission")
@Composable
fun BluetoothDeviceItem(device: BluetoothDevice, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurfaceVariant),
        shape = MaterialTheme.shapes.small
    ) {
        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
            Text(text = device.name ?: "Unknown Device", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            Text(text = device.address, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
    }
}

@Composable
fun StatusCard(connectedDevice: String?, errorMessage: String?) {
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Status", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            if (connectedDevice != null) {
                Text(text = "Connected to: $connectedDevice", color = Color(0xFF006400))
            } else {
                Text(text = "Not Connected! Try again!")
            }
            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Error: $errorMessage", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
@Composable
fun UserCard(user: User) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            user.phone?.let { phone ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Phone: $phone",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}
