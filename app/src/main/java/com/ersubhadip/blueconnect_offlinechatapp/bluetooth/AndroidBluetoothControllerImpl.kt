package com.ersubhadip.blueconnect_offlinechatapp.bluetooth

import android.Manifest
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import com.ersubhadip.blueconnect_offlinechatapp.mappers.toBluetoothDeviceModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AndroidBluetoothControllerImpl(
    private val context: Context
) : BluetoothController {

    private val _scannedDevices = MutableStateFlow<List<BluetoothDeviceModel>>(emptyList())
    private val _pairedDevices = MutableStateFlow<List<BluetoothDeviceModel>>(emptyList())

    private val bluetoothManager by lazy {
        context.getSystemService(BluetoothManager::class.java)
    }

    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    override val scannedDevices: StateFlow<List<BluetoothDeviceModel>>
        get() = _scannedDevices.asStateFlow()
    override val pairedDevices: StateFlow<List<BluetoothDeviceModel>>
        get() = _pairedDevices.asStateFlow()

    override fun startDiscovery() {
        TODO("Not yet implemented")
    }

    override fun stopDiscovery() {
        TODO("Not yet implemented")
    }

    override fun releaseController() {
        TODO("Not yet implemented")
    }

    private fun updatePairedDevice() {
        if (hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
            bluetoothAdapter?.bondedDevices?.map {
                it.toBluetoothDeviceModel()
            }.also { _pairedDevices.update { it } }
        }
    }

    private fun hasPermission(permission: String): Boolean {
        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }
}