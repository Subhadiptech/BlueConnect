package com.ersubhadip.blueconnect_offlinechatapp.domain.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import com.ersubhadip.blueconnect_offlinechatapp.mappers.toBluetoothDeviceModel
import com.ersubhadip.blueconnect_offlinechatapp.receivers.BluetoothConnectionStateReceiver
import com.ersubhadip.blueconnect_offlinechatapp.receivers.FoundDeviceReceiver
import com.ersubhadip.blueconnect_offlinechatapp.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.UUID

@SuppressLint("MissingPermission")
class AndroidBluetoothControllerImpl(
    private val context: Context
) : BluetoothController {

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

    private val bluetoothStateChangeReceiver =
        BluetoothConnectionStateReceiver { isConnected, device ->
            if (bluetoothAdapter?.bondedDevices?.contains(device) == true) {
                _isConnected.update { isConnected }
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    _error.emit("Pairing unsuccessful")
                }
            }
        }

    private var currentServerSocket: BluetoothServerSocket? = null
    private var currentClientSocket: BluetoothSocket? = null

    init {
        updatePairedDevice()
        context.registerReceiver(
            bluetoothStateChangeReceiver,
            IntentFilter().apply {
                addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
                addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
                addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
            }
        )
    }

    private val _isConnected = MutableStateFlow(false)
    override val isConnected: StateFlow<Boolean>
        get() = _isConnected.asStateFlow()

    private val _error = MutableSharedFlow<String>()
    override val error: SharedFlow<String>
        get() = _error.asSharedFlow()

    private val _scannedDevices = MutableStateFlow<List<BluetoothDeviceModel>>(emptyList())
    override val scannedDevices: StateFlow<List<BluetoothDeviceModel>>
        get() = _scannedDevices.asStateFlow()

    private val _pairedDevices = MutableStateFlow<List<BluetoothDeviceModel>>(emptyList())
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

    override fun startBluetoothServer(): Flow<ConnectionResult> = flow {
        if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
            throw SecurityException("No Bluetooth Connect found!")
        } else {
            currentServerSocket = bluetoothAdapter?.listenUsingRfcommWithServiceRecord(
                "chat_service",
                UUID.fromString(Constants.SERVICE_UUID)
            )
            var shouldLoop = true
            while (shouldLoop) {
                currentClientSocket = try {
                    currentServerSocket?.accept()
                } catch (err: IOException) {
                    shouldLoop = false
                    null
                }
            }
            emit(ConnectionResult.ConnectionEstablished)
            currentClientSocket?.let {
                currentServerSocket?.close()
            }

        }
    }.onCompletion {
        closeConnection()
    }.flowOn(Dispatchers.IO)

    override fun connectToDevice(device: BluetoothDeviceModel): Flow<ConnectionResult> = flow {
        if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
            throw SecurityException("No Bluetooth Connect found!")
        }
        currentClientSocket = bluetoothAdapter
            ?.getRemoteDevice(device.macAddress)
            ?.createRfcommSocketToServiceRecord(UUID.fromString(Constants.SERVICE_UUID))

        stopDiscovery()
        currentClientSocket?.let { socket ->
            try {
                socket.connect()
                emit(ConnectionResult.ConnectionEstablished)
            } catch (err: IOException) {
                socket.close()
                currentClientSocket = null
                emit(ConnectionResult.ConnectionFailed("Connection was interrupted"))

            }
        }
    }.onCompletion {
        closeConnection()
    }.flowOn(Dispatchers.IO)

    override fun closeConnection() {
        currentServerSocket?.close()
        currentClientSocket?.close()
        currentServerSocket = null
        currentClientSocket = null
    }

    override fun releaseController() {
        context.unregisterReceiver(foundDeviceReceiver)
        context.unregisterReceiver(bluetoothStateChangeReceiver)
        closeConnection()
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