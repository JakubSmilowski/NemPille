package com.example.nempille.ui.screens.settings

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nempille.ui.bluetoothConnections.NemPilleManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BluetoothViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    // 1. UI State (Backing field)
    private val _uiState = MutableStateFlow(BluetoothUiState())
    val uiState: StateFlow<BluetoothUiState> = _uiState.asStateFlow()

    // 2. Bluetooth Helpers
    private val bluetoothManager = application.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
    private val nemPilleManager = NemPilleManager(application) // Our helper class

    private val scanHandler = Handler(Looper.getMainLooper())

    // 3. Scanning Logic
    @SuppressLint("MissingPermission")
    fun scanForDevices() {
        if (_uiState.value.isScanning) {
            stopScan()
        } else {
            startScan()
        }
    }

    @SuppressLint("MissingPermission")
    private fun startScan() {
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
            _uiState.update { it.copy(errorMessage = "Bluetooth is off") }
            return
        }

        _uiState.update { it.copy(isScanning = true, scannedDevices = emptyList()) }

        // Start scanning
        bluetoothAdapter.bluetoothLeScanner?.startScan(scanCallback)

        // Stop automatically after 5 seconds
        scanHandler.postDelayed({ stopScan() }, 5000)
    }

    @SuppressLint("MissingPermission")
    private fun stopScan() {
        bluetoothAdapter?.bluetoothLeScanner?.stopScan(scanCallback)
        _uiState.update { it.copy(isScanning = false) }
    }

    // Callback when a device is found
    private val scanCallback = object : ScanCallback() {
        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val device = result.device
            // Filter: Only add if it has a name (or specifically "NemPille")
            if (device.name != null) {
                // Add to list if not already there
                val currentList = _uiState.value.scannedDevices
                if (currentList.none { it.address == device.address }) {
                    _uiState.update {
                        it.copy(scannedDevices = currentList + device)
                    }
                }
            }
        }
    }

    // 4. Connection Logic
    @SuppressLint("MissingPermission")
    fun connectToDevice(device: BluetoothDevice) {
        stopScan() // Stop scanning before connecting

        _uiState.update { it.copy(connectedDevice = "Connecting to ${device.name}...") }

        // Use our Manager to connect
        nemPilleManager.connectToDevice(device)

        // (Optional) You could update the UI state when connected
        // This requires NemPilleManager to have a callback,
        // but for now, we assume it works.
        _uiState.update { it.copy(connectedDevice = device.name) }
    }
}