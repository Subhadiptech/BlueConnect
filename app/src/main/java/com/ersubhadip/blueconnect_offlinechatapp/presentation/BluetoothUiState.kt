package com.ersubhadip.blueconnect_offlinechatapp.presentation

import com.ersubhadip.blueconnect_offlinechatapp.domain.bluetooth.BluetoothDeviceModel

data class BluetoothUiState(
    val isConnected: Boolean = false,
    val isConnecting: Boolean = false,
    val errorMessage: String? = null,
    val scannedDevices: List<BluetoothDeviceModel> = emptyList(),
    val pairedDevices: List<BluetoothDeviceModel> = emptyList()
)
