package org.nitish.project.sharedrop

actual class FileSaver {
    actual fun moveFile(
        fileName: String,
        sourcePath: String,
        onResult: (success: Boolean, filePath: String) -> Unit
    ) {
    }
}
