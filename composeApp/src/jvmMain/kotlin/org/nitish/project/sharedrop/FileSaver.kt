package org.nitish.project.sharedrop

import java.io.File

actual class FileSaver {

    actual fun moveFile(
        fileName: String,
        sourcePath: String,
        onResult: (success: Boolean, filePath: String) -> Unit
    ) {
        runCatching {
            val sourceFile = File(sourcePath)

            val downloadsDir = File(System.getProperty("user.home"), "Downloads")
            if (!downloadsDir.exists()) {
                downloadsDir.mkdirs()
            }

            val outputFile = File(downloadsDir, fileName)
            sourceFile.copyTo(
                target = outputFile,
                overwrite = true
            ).also {
                sourceFile.delete()
            }
        }.onFailure {
            onResult(false, "")
        }.onSuccess {
            onResult(true, it.absolutePath)
        }
    }
}
