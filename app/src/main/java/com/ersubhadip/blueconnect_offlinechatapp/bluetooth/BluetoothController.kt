package com.ersubhadip.blueconnect_offlinechatapp.bluetooth

import kotlinx.coroutines.flow.StateFlow

interface BluetoothController {
    val scannedDevices: StateFlow<List<BluetoothDeviceModel>>
    val pairedDevices: StateFlow<List<BluetoothDeviceModel>>

    fun startDiscovery()
    fun stopDiscovery()

    fun releaseController()

}