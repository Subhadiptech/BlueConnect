package com.ersubhadip.blueconnect_offlinechatapp.mappers


import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import com.ersubhadip.blueconnect_offlinechatapp.bluetooth.BluetoothDeviceModel

@SuppressLint("MissingPermission")
fun BluetoothDevice.toBluetoothDeviceModel(): BluetoothDeviceModel = BluetoothDeviceModel(
    name = name,
    macAddress = address
)