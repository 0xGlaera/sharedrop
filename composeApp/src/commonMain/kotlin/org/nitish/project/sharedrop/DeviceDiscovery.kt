package org.nitish.project.sharedrop

data class DiscoveredDevice(
    val name: String,
    val host: String,
    val port: Int
)

expect class DeviceDiscovery() {
    fun startDiscovery(onDeviceFound: (DiscoveredDevice) -> Unit)
    fun stopDiscovery()
}