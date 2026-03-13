package org.nitish.project.sharedrop

expect class FileSender() {
    fun sendFile(host: String, port: Int, fileName: String, bytes: ByteArray, onResult: (Boolean) -> Unit)
}
