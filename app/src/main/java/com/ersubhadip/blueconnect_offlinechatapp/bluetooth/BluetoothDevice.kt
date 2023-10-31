package com.ersubhadip.blueconnect_offlinechatapp.bluetooth


typealias BluetoothDeviceModel = BluetoothDevice

data class BluetoothDevice(
    val name: String,
    val macAddress: String
)
