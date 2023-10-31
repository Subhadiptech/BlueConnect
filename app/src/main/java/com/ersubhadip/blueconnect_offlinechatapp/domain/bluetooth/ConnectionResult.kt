package com.ersubhadip.blueconnect_offlinechatapp.domain.bluetooth

sealed interface ConnectionResult {
    data object ConnectionEstablished : ConnectionResult
    data class ConnectionFailed(val message: String) : ConnectionResult
}