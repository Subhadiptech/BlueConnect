package com.ersubhadip.blueconnect_offlinechatapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ersubhadip.blueconnect_offlinechatapp.ui.theme.BlueConnectOfflineChatAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BlueConnectOfflineChatAppTheme {

            }
        }
    }
}