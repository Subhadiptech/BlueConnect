package com.ersubhadip.blueconnect_offlinechatapp.domain.bluetooth


typealias BluetoothDeviceModel = BluetoothDevice

data class BluetoothDevice(
    val name: String,
    val macAddress: String
)
