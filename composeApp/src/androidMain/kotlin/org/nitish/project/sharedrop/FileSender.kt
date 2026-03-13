package org.nitish.project.sharedrop

import java.net.Socket

actual class FileSender {
    actual fun sendFile(
        host: String,
        port: Int,
        fileName: String,
        bytes: ByteArray,
        onResult: (Boolean) -> Unit
    ) {
        Thread {
            try {
                val socket = Socket(host, port)
                val output = socket.getOutputStream()
                val fileNameBytes = fileName.toByteArray()
                output.write(fileNameBytes.size)
                output.write(fileNameBytes)
                output.write(bytes)
                output.flush()
                socket.close()
                onResult(true)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false)
            }
        }.start()
    }
}