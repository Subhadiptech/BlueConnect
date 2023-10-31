package com.ersubhadip.blueconnect_offlinechatapp.presentation

import com.ersubhadip.blueconnect_offlinechatapp.bluetooth.BluetoothDeviceModel

data class BluetoothUiState(
    val scannedDevices: List<BluetoothDeviceModel> = emptyList(),
    val pairedDevices: List<BluetoothDeviceModel> = emptyList()
)
