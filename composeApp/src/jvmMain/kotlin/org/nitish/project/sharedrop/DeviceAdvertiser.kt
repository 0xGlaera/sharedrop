package org.nitish.project.sharedrop

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

actual class DeviceAdvertiser {
    private var isRunning = false
    private var socket: DatagramSocket? = null

    actual fun startAdvertising(deviceName: String, port: Int) {
        isRunning = true
        Thread {
            try {
                socket = DatagramSocket()
                socket?.broadcast = true
                val broadcastAddress = InetAddress.getByName("255.255.255.255")
                while (isRunning) {
                    val message = "SHAREDROP:$deviceName:$port"
                    val data = message.toByteArray()
                    val packet = DatagramPacket(data, data.size, broadcastAddress, 8888)
                    socket?.send(packet)
                    println("Advertiser: Sent broadcast $message")
                    Thread.sleep(2000)
                }
            } catch (e: Exception) {
                if (isRunning) e.printStackTrace()
            }
        }.start()
    }

    actual fun stopAdvertising() {
        isRunning = false
        socket?.close()
        socket = null
    }
}
