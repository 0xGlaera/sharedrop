package org.nitish.project.sharedrop

actual class FileSaver {
    actual fun saveFile(
        fileName: String,
        bytes: ByteArray,
        onResult: (success: Boolean, filePath: String) -> Unit
    ) {
        onResult(false, "")
    }
}
