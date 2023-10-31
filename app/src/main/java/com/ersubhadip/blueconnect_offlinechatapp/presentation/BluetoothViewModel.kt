package com.ersubhadip.blueconnect_offlinechatapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ersubhadip.blueconnect_offlinechatapp.domain.bluetooth.BluetoothController
import com.ersubhadip.blueconnect_offlinechatapp.domain.bluetooth.BluetoothDeviceModel
import com.ersubhadip.blueconnect_offlinechatapp.domain.bluetooth.ConnectionResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BluetoothViewModel @Inject constructor(
    private val bluetoothControllerImpl: BluetoothController
) : ViewModel() {

    private var _state = MutableStateFlow(BluetoothUiState())

    private var deviceConnectionJob: Job? = null

    val state = combine(
        bluetoothControllerImpl.scannedDevices,
        bluetoothControllerImpl.pairedDevices,
        _state
    ) { scanned, paired, state ->
        state.copy(
            scannedDevices = scanned,
            pairedDevices = paired
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)


    init {
        bluetoothControllerImpl.isConnected.onEach { isConnected ->
            _state.update { it.copy(isConnected = isConnected) }
        }.launchIn(viewModelScope)
        bluetoothControllerImpl.error.onEach { error ->
            _state.update { it.copy(errorMessage = error) }
        }.launchIn(viewModelScope)
    }


    fun startScan() {
        bluetoothControllerImpl.startDiscovery()
    }

    fun stopScan() {
        bluetoothControllerImpl.stopDiscovery()
    }

    fun connectToDevice(device: BluetoothDeviceModel) {
        _state.update { it.copy(isConnecting = true) }
        deviceConnectionJob = bluetoothControllerImpl
            .connectToDevice(device)
            .listen()
    }

    fun disconnectFromDevice() {
        deviceConnectionJob?.cancel()
        bluetoothControllerImpl.closeConnection()
        _state.update {
            it.copy(
                isConnecting = false,
                isConnected = false
            )
        }
    }

    fun waitForIncomingConnections() {
        _state.update { it.copy(isConnecting = true) }
        deviceConnectionJob = bluetoothControllerImpl
            .startBluetoothServer()
            .listen()
    }


    private fun Flow<ConnectionResult>.listen(): Job {
        return onEach { result ->
            when (result) {
                ConnectionResult.ConnectionEstablished -> {
                    _state.update {
                        it.copy(
                            isConnected = true,
                            isConnecting = false,
                            errorMessage = null
                        )
                    }
                }

                is ConnectionResult.ConnectionFailed -> {
                    _state.update {
                        it.copy(
                            isConnected = false,
                            isConnecting = false,
                            errorMessage = result.message
                        )
                    }
                }
            }
        }
            .catch { throwable ->
                bluetoothControllerImpl.closeConnection()
                _state.update {
                    it.copy(
                        isConnected = false,
                        isConnecting = false,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        bluetoothControllerImpl.releaseController()
    }
}