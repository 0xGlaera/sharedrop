package org.nitish.project.sharedrop

import java.net.ServerSocket
import java.net.Socket

actual class FileReceiver {
    private var serverSocket: ServerSocket? = null
    private var isRunning = false

    actual fun startReceiving(port: Int, onFileReceived: (fileName: String, bytes: ByteArray) -> Unit) {
        Thread {
            try {
                serverSocket = ServerSocket(port)
                isRunning = true
                while (isRunning) {
                    val client: Socket = serverSocket?.accept() ?: break
                    Thread {
                        try {
                            val input = client.getInputStream()
                            val fileNameLength = input.read()
                            val fileNameBytes = ByteArray(fileNameLength)
                            input.read(fileNameBytes)
                            val fileName = String(fileNameBytes)
                            val fileBytes = input.readBytes()
                            onFileReceived(fileName, fileBytes)
                            client.close()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }.start()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    actual fun stopReceiving() {
        isRunning = false
        serverSocket?.close()
        serverSocket = null
    }
}