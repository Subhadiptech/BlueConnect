package com.ersubhadip.blueconnect_offlinechatapp.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ersubhadip.blueconnect_offlinechatapp.domain.bluetooth.BluetoothDeviceModel
import com.ersubhadip.blueconnect_offlinechatapp.presentation.BluetoothUiState
import com.ersubhadip.blueconnect_offlinechatapp.ui.theme.PrimaryGray
import com.ersubhadip.blueconnect_offlinechatapp.ui.theme.PrimaryPurple
import com.ersubhadip.blueconnect_offlinechatapp.ui.theme.PrimaryTextColor
import com.ersubhadip.blueconnect_offlinechatapp.ui.theme.PrimaryWhite


enum class CurrentTab {
    Paired, Scanned
}

@Composable
fun DeviceScreen(
    state: BluetoothUiState,
    onStartScan: () -> Unit,
    onStopScan: () -> Unit,
    onStartServer: () -> Unit,
    onDeviceClick: (BluetoothDeviceModel) -> Unit
) {
    var currentTabState by rememberSaveable {
        mutableStateOf(CurrentTab.Paired)
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomEnd = 28.dp, bottomStart = 28.dp))
                .background(PrimaryPurple)
                .padding(top = 16.dp, bottom = 36.dp)
        ) {
            CustomText(
                text = "BlueConnect",
                fontSize = 24.sp,
                color = PrimaryWhite,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            CustomText(
                text = "Offline Chat App",
                fontSize = 14.sp,
                color = PrimaryTextColor,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        TabView(selectedTab = currentTabState) { selectedTab ->
            currentTabState = selectedTab
        }

        when (currentTabState) {
            CurrentTab.Paired -> {
                BluetoothDeviceList(
                    deviceList = state.pairedDevices,
                    onClick = onDeviceClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                ScannedCTA(onStartServer)
            }

            CurrentTab.Scanned -> {
                BluetoothDeviceList(
                    deviceList = state.scannedDevices,
                    onClick = onDeviceClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                PairedCTA(onStartScan, onStopScan)
            }
        }
    }
}


@Composable
fun PairedCTA(
    onStartScan: () -> Unit,
    onStopScan: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Button(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            border = BorderStroke(width = 2.dp, color = PrimaryPurple),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
            onClick = onStartScan
        ) {
            CustomText("Search Devices")
        }
        Spacer(modifier = Modifier.height(4.dp))
        Button(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            border = BorderStroke(width = 1.dp, color = PrimaryPurple),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            onClick = onStopScan
        ) {
            CustomText("Stop Searching", color = PrimaryPurple)
        }
    }
}

@Composable
fun ScannedCTA(
    onStartServer: () -> Unit
) {
    Button(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp, horizontal = 16.dp),
        border = BorderStroke(width = 2.dp, color = PrimaryPurple),
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
        onClick = onStartServer
    ) {
        CustomText("Start chatting")
    }

}


@Composable
fun TabView(
    selectedTab: CurrentTab = CurrentTab.Paired,
    onTabChangedListener: (CurrentTab) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        CustomText(
            text = "Paired",
            color = if (selectedTab == CurrentTab.Paired) PrimaryGray else PrimaryTextColor,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .border(width = 2.dp, color = PrimaryTextColor, shape = RoundedCornerShape(20.dp))
                .background(if (selectedTab == CurrentTab.Paired) PrimaryTextColor else Color.Transparent)
                .weight(1f)
                .clickable {
                    onTabChangedListener(CurrentTab.Paired)
                }
                .padding(vertical = 8.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        CustomText(
            text = "Scanned",
            color = if (selectedTab == CurrentTab.Scanned) PrimaryGray else PrimaryTextColor,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .border(width = 2.dp, color = PrimaryTextColor, shape = RoundedCornerShape(20.dp))
                .background(if (selectedTab == CurrentTab.Scanned) PrimaryTextColor else Color.Transparent)
                .weight(1f)
                .clickable {
                    onTabChangedListener(CurrentTab.Scanned)
                }
                .padding(vertical = 8.dp)
        )
    }
}


@Composable
fun BluetoothDeviceList(
    deviceList: List<BluetoothDeviceModel>,
    onClick: (BluetoothDeviceModel) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(deviceList) { device ->
            CustomText(
                text = device.name,
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth().clickable {
                    onClick(device)
                }.padding(16.dp)
            )
        }
    }

}