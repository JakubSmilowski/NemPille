package com.example.nempille.ui.bluetoothConnections

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import android.util.Log

class NemPilleManager(private val context: Context) {

    private var bluetoothGatt: BluetoothGatt? = null

    private val gattCallback = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.d("NemPille", "Connected to GATT server.")
                // Important: Discover services after connection
                gatt.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.d("NemPille", "Disconnected from GATT server.")
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d("NemPille", "Services discovered. Ready for commands.")
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun connectToDevice(device: BluetoothDevice) {
        bluetoothGatt = device.connectGatt(context, false, gattCallback)
    }

    @SuppressLint("MissingPermission")
    fun disconnect() {
        bluetoothGatt?.close()
        bluetoothGatt = null
    }

    @SuppressLint("MissingPermission")
    fun setLedColor(colorCode: Int) {
        val service = bluetoothGatt?.getService(NemPilleUUIDs.SERVICE_UUID)
        val characteristic = service?.getCharacteristic(NemPilleUUIDs.LED_CHAR_UUID)

        if (characteristic != null) {
            // Write the value (1=Red, 2=Green, etc.)
            characteristic.value = byteArrayOf(colorCode.toByte())
            bluetoothGatt?.writeCharacteristic(characteristic)
        }
    }

    @SuppressLint("MissingPermission")
    fun sendMedicationText(text: String) {
        val service = bluetoothGatt?.getService(NemPilleUUIDs.SERVICE_UUID)
        val characteristic = service?.getCharacteristic(NemPilleUUIDs.LCD_FIRSTLINE_CHAR_UUID)

        if (characteristic != null) {
            characteristic.value = text.toByteArray(Charsets.UTF_8)
            bluetoothGatt?.writeCharacteristic(characteristic)
        }
    }
}