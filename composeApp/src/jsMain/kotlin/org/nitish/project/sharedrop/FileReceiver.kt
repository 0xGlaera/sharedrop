package org.nitish.project.sharedrop

actual class FileReceiver {
    actual fun startReceiving(port: Int, onFileReceived: (fileName: String, bytes: ByteArray) -> Unit) {
    }
    actual fun stopReceiving() {
    }
}