package org.nitish.project.sharedrop

actual class FileSender {
    actual fun sendFile(host: String, port: Int, fileName: String, bytes: ByteArray, onResult: (Boolean) -> Unit) {
    }
}