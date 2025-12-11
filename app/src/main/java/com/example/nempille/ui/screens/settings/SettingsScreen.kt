package com.example.nempille.ui.screens.settings

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@SuppressLint("MissingPermission") // Suppress permission warning for device.name
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: BluetoothViewModel = hiltViewModel()
) {
    // 1. Observe the UI State from ViewModel
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // --- HEADER: Title & Scan Switch ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Bluetooth Settings",
                style = MaterialTheme.typography.headlineSmall
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = if (uiState.isScanning) "Scanning..." else "Scan")
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = uiState.isScanning,
                    onCheckedChange = { viewModel.scanForDevices() }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- LOADING INDICATOR ---
        if (uiState.isScanning) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
        }

        // --- DEVICE LIST ---
        Text(
            text = "Available Devices:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f) // Takes up remaining space
                .fillMaxWidth()
        ) {
            items(uiState.scannedDevices) { device ->
                BluetoothDeviceItem(
                    device = device,
                    onClick = { viewModel.connectToDevice(device) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- FOOTER: Connection Status ---
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Status",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )

                if (uiState.connectedDevice != null) {
                    Text(
                        text = "✅ Connected to: ${uiState.connectedDevice}",
                        color = Color(0xFF006400) // Dark Green
                    )
                } else {
                    Text(text = "❌ Not Connected")
                }

                if (uiState.errorMessage != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Error: ${uiState.errorMessage}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

// --- HELPER COMPONENT: Single Device Row ---
@SuppressLint("MissingPermission")
@Composable
fun BluetoothDeviceItem(
    device: BluetoothDevice,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = MaterialTheme.shapes.small
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Display Name (or "Unknown" if null)
            Text(
                text = device.name ?: "Unknown Device",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            // Display MAC Address (e.g., AA:BB:CC:11:22)
            Text(
                text = device.address,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}