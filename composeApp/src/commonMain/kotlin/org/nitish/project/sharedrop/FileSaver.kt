package org.nitish.project.sharedrop

expect class FileSaver() {
    fun saveFile(fileName: String, bytes: ByteArray, onResult: (success: Boolean, filePath: String) -> Unit)
}
