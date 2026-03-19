package org.nitish.project.sharedrop

expect class FileReceiver() {
    fun startReceiving(
        port: Int,
        onProgress: (fileName : String, progress: Float) -> Unit,
        onFileReceived: (fileName: String, tempFilePath: String) -> Unit
    )

    fun stopReceiving()
}