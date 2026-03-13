package org.nitish.project.sharedrop

import java.io.File

actual class FileSaver {
    actual fun saveFile(
        fileName: String,
        bytes: ByteArray,
        onResult: (success: Boolean, filePath: String) -> Unit
    ) {
        runCatching {
            val downloadsDir = File(System.getProperty("user.home"), "Downloads")
            if (!downloadsDir.exists()) {
                downloadsDir.mkdirs()
            }

            val outputFile = File(downloadsDir, fileName)
            outputFile.writeBytes(bytes)
            outputFile.absolutePath
        }.onSuccess { filePath ->
            onResult(true, filePath)
        }.onFailure {
            onResult(false, "")
        }
    }
}
