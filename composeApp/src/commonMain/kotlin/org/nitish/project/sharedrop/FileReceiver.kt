package org.nitish.project.sharedrop

expect class FileReceiver() {
    fun startReceiving(port: Int, onFileReceived: (fileName: String, bytes: ByteArray) -> Unit)
    fun stopReceiving()
}