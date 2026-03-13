package org.nitish.project.sharedrop

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

actual class DeviceDiscovery {
    private var isRunning = false
    private var socket: DatagramSocket? = null

    actual fun startDiscovery(onDeviceFound: (DiscoveredDevice) -> Unit) {
        isRunning = true
        Thread {
            try {
                socket = DatagramSocket(8888, InetAddress.getByName("0.0.0.0"))
                socket?.broadcast = true
                val buffer = ByteArray(1024)
                while (isRunning) {
                    val packet = DatagramPacket(buffer, buffer.size)
                    socket?.receive(packet)
                    val message = String(packet.data, 0, packet.length)
                    if (message.startsWith("SHAREDROP:")) {
                        val parts = message.split(":")
                        if (parts.size >= 3) {
                            val name = parts[1]
                            val port = parts[2].toIntOrNull() ?: 8080
                            val host = packet.address.hostAddress ?: continue
                            println("Discovery: Found $name at $host:$port")
                            val localIp = java.net.NetworkInterface.getNetworkInterfaces()
                                .asSequence()
                                .flatMap { it.inetAddresses.asSequence() }
                                .firstOrNull { !it.isLoopbackAddress && it is java.net.Inet4Address }
                                ?.hostAddress
                            if (host == localIp) continue
                            android.os.Handler(android.os.Looper.getMainLooper()).post {
                                onDeviceFound(DiscoveredDevice(name = name, host = host, port = port))
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                if (isRunning) e.printStackTrace()
            }
        }.start()
    }

    actual fun stopDiscovery() {
        isRunning = false
        socket?.close()
        socket = null
    }
}
