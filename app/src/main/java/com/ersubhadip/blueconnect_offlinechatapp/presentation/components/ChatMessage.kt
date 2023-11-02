package com.ersubhadip.blueconnect_offlinechatapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ersubhadip.blueconnect_offlinechatapp.domain.bluetooth.BluetoothMessage
import com.ersubhadip.blueconnect_offlinechatapp.ui.theme.PrimaryGray
import com.ersubhadip.blueconnect_offlinechatapp.ui.theme.PrimaryPurple
import com.ersubhadip.blueconnect_offlinechatapp.ui.theme.PrimaryTextColor
import com.ersubhadip.blueconnect_offlinechatapp.ui.theme.PrimaryWhite

@Composable
fun ChatMessage(
    message: BluetoothMessage,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(
                RoundedCornerShape(
                    topStart = if (message.isFromLocalUser) 15.dp else 0.dp,
                    topEnd = 15.dp,
                    bottomStart = 15.dp,
                    bottomEnd = if (message.isFromLocalUser) 0.dp else 15.dp
                )
            )
            .background(
                if (message.isFromLocalUser) PrimaryPurple else PrimaryGray
            )
            .padding(16.dp)

    ) {
        CustomText(
            text = message.message,
            color = if (message.isFromLocalUser) PrimaryWhite else PrimaryTextColor,
            modifier = Modifier.widthIn(max = 250.dp)
        )
    }
}

@Preview
@Composable
fun ChatMessagePreview() {
    ChatMessage(
        message = BluetoothMessage(
            message = "Hello World!",
            sendersName = "Pixel 6",
            isFromLocalUser = true
        )
    )
}