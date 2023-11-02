package com.ersubhadip.blueconnect_offlinechatapp.domain.bluetooth

data class BluetoothMessage(
    val message: String,
    val sendersName: String,
    val isFromLocalUser: Boolean,
)