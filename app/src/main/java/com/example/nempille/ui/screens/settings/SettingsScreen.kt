package com.example.nempille.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: BluetoothViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Bluetooth")
            Switch(
                checked = uiState.isScanning,
                onCheckedChange = { viewModel.scanForDevices() }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (uiState.isScanning) {
            CircularProgressIndicator()
        }
        LazyColumn {
            items(uiState.scannedDevices) { device ->
                Button(
                    onClick = { viewModel.connectToDevice(device) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = device)
                }
            }
        }
        uiState.connectedDevice?.let {
            Text(text = "Connected to: $it")
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.sendData("Hello World") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Send Hello World")
            }
        }
        uiState.errorMessage?.let {
            Text(text = "Error: $it")
        }
    }
}
