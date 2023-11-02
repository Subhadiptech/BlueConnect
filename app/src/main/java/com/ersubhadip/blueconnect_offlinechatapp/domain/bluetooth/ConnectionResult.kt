package com.ersubhadip.blueconnect_offlinechatapp.domain.bluetooth

sealed interface ConnectionResult {
    data object ConnectionEstablished : ConnectionResult
    data class TransferSucceeded(val message: BluetoothMessage) : ConnectionResult
    data class ConnectionFailed(val message: String) : ConnectionResult
}