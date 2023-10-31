package com.ersubhadip.blueconnect_offlinechatapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ersubhadip.blueconnect_offlinechatapp.bluetooth.BluetoothController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BluetoothViewModel @Inject constructor(
    private val bluetoothControllerImpl: BluetoothController
) : ViewModel() {

    private var _state = MutableStateFlow(BluetoothUiState())
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


    fun startScan() {
        bluetoothControllerImpl.startDiscovery()
    }

    fun stopScan() {
        bluetoothControllerImpl.stopDiscovery()
    }
}