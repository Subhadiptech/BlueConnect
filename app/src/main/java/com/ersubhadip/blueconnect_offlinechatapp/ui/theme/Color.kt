package com.ersubhadip.blueconnect_offlinechatapp.ui.theme

import androidx.compose.ui.graphics.Color


val PrimaryPurple = "#5f5eda".toColor()
val PrimaryGray = "#f0f5f9".toColor()
val PrimaryGreen = "#77cda7".toColor()
val PrimaryWhite = "#ffffff".toColor()
val PrimaryBlack = "#000000".toColor()
val PrimaryTextColor = "#8b9da9".toColor()

private fun String.toColor() = Color(android.graphics.Color.parseColor(this))