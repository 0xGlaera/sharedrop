package org.nitish.project.sharedrop

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun App() {
    MaterialTheme {
        HomeScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val localDeviceName = remember { getDeviceName() }
    val localPlatformPrefix = remember(localDeviceName) { "${localDeviceName.substringBefore('-')}-" }
    val nearbyDevices = remember { mutableStateListOf<DiscoveredDevice>() }
    val discovery = remember { DeviceDiscovery() }
    val advertiser = remember { DeviceAdvertiser() }
    val receiver = remember { FileReceiver() }
    val scope = rememberCoroutineScope()
    val receivedFiles = remember { mutableStateListOf<String>() }
    var selectedDevice by remember { mutableStateOf<DiscoveredDevice?>(null) }
    var statusMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        advertiser.startAdvertising(localDeviceName, 8080)
        discovery.startDiscovery { device ->
            scope.launch {
                withContext(Dispatchers.Main) {
                    if (!device.name.startsWith(localPlatformPrefix) && nearbyDevices.none { it.host == device.host }) {
                        nearbyDevices.add(device)
                    }
                }
            }
        }
        receiver.startReceiving(8080) { fileName, bytes ->
            FileSaver().saveFile(fileName, bytes) { success, path ->
                receivedFiles.add(fileName)
                statusMessage = if (success) {
                    "Saved: $fileName to $path"
                } else {
                    "Received but failed to save: $fileName"
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            advertiser.stopAdvertising()
            discovery.stopDiscovery()
            receiver.stopReceiving()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ShareDrop") }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (statusMessage.isNotEmpty()) {
                    Text(
                        text = statusMessage,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                val filePicker = remember { FilePicker() }

                Button(
                    onClick = {
                        val device = selectedDevice
                        if (device == null) {
                            statusMessage = "Please select a device first!"
                        } else {
                            filePicker.pickFile { fileName, bytes ->
                                statusMessage = "Sending $fileName..."
                                FileSender().sendFile(
                                    host = device.host,
                                    port = device.port,
                                    fileName = fileName,
                                    bytes = bytes
                                ) { success ->
                                    statusMessage = if (success) "Sent $fileName!" else "Failed to send $fileName!"
                                }
                            }
                        }
                    }
                ) {
                    Text(if (selectedDevice == null) "Select a device to send" else "Send File to ${selectedDevice!!.name}")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Nearby Devices",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (nearbyDevices.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Searching for devices...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn {
                    items(nearbyDevices) { device ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    selectedDevice = if (selectedDevice == device) null else device
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedDevice == device)
                                    MaterialTheme.colorScheme.primaryContainer
                                else
                                    MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = device.name,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    text = "${device.host}:${device.port}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                if (receivedFiles.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Received Files",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn {
                        items(receivedFiles) { fileName ->
                            Text(
                                text = "📄 $fileName",
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
