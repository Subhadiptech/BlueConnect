package com.ersubhadip.blueconnect_offlinechatapp.mappers

import com.ersubhadip.blueconnect_offlinechatapp.domain.bluetooth.BluetoothMessage

fun BluetoothMessage.toByteArray(): ByteArray = "$sendersName&^&^&$message".encodeToByteArray()

fun String.toBluetoothMessage(isFromLocalUser: Boolean): BluetoothMessage {
    val name = substringBeforeLast("&^&^&")
    val message = substringAfterLast("&^&^&")
    return BluetoothMessage(
        message = message,
        sendersName = name,
        isFromLocalUser = isFromLocalUser
    )
}