package com.example.nempille.ui.screens.settings

import android.bluetooth.BluetoothDevice

data class BluetoothUiState(
    val isScanning: Boolean = false,
    val scannedDevices: List<BluetoothDevice> = emptyList(),
    val connectedDevice: String? = null,
    val errorMessage: String? = null
)