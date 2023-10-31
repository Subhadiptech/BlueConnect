package com.ersubhadip.blueconnect_offlinechatapp.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ersubhadip.blueconnect_offlinechatapp.bluetooth.BluetoothDeviceModel
import com.ersubhadip.blueconnect_offlinechatapp.presentation.BluetoothUiState

@Composable
fun DeviceScreen(
    state: BluetoothUiState,
    onStartScan: () -> Unit,
    onStopScan: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        BluetoothDeviceList(
            pairedDevice = state.pairedDevices,
            scannedDevice = state.scannedDevices,
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = onStartScan) {
                Text("Start Scan")
            }
            Button(onClick = onStopScan) {
                Text("Stop Scan")
            }
        }
    }
}


@Composable
fun BluetoothDeviceList(
    pairedDevice: List<BluetoothDeviceModel>,
    scannedDevice: List<BluetoothDeviceModel>,
    onClick: (BluetoothDeviceModel) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyColumn(modifier = modifier) {
        item {
            Text(
                text = "Paired Devices",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )
        }

        items(pairedDevice) { device ->
            Text(
                text = device.name,
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth().clickable {
                    onClick(device)
                }.padding(16.dp)
            )
        }
        item {
            Text(
                text = "Scanned Devices",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )
        }

        items(scannedDevice) { device ->
            Text(
                text = device.name,
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth().clickable {
                    onClick(device)
                }.padding(16.dp)
            )
        }
    }

}