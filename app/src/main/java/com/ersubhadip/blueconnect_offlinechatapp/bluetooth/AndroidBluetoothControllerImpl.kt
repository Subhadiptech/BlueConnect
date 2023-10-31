package com.ersubhadip.blueconnect_offlinechatapp.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import com.ersubhadip.blueconnect_offlinechatapp.mappers.toBluetoothDeviceModel
import com.ersubhadip.blueconnect_offlinechatapp.receivers.FoundDeviceReceiver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@SuppressLint("MissingPermission")
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
    private val foundDeviceReceiver = FoundDeviceReceiver { device ->
        _scannedDevices.update { devices ->
            val newDevice = device.toBluetoothDeviceModel()
            if (newDevice in devices) devices else devices + newDevice
        }
    }

    init {
        updatePairedDevice()
    }

    override val scannedDevices: StateFlow<List<BluetoothDeviceModel>>
        get() = _scannedDevices.asStateFlow()
    override val pairedDevices: StateFlow<List<BluetoothDeviceModel>>
        get() = _pairedDevices.asStateFlow()

    override fun startDiscovery() {
        if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) return

        context.registerReceiver(
            foundDeviceReceiver,
            IntentFilter(android.bluetooth.BluetoothDevice.ACTION_FOUND)
        )

        updatePairedDevice()
        bluetoothAdapter?.startDiscovery()
    }

    override fun stopDiscovery() {
        if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) return

        bluetoothAdapter?.cancelDiscovery()
    }

    override fun releaseController() {
        context.unregisterReceiver(foundDeviceReceiver)
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