package org.nitish.project.sharedrop

expect class DeviceAdvertiser() {
    fun startAdvertising(deviceName: String, port: Int)
    fun stopAdvertising()
}