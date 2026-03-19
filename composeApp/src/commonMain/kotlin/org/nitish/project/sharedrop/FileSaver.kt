package org.nitish.project.sharedrop

expect class FileSaver() {
    fun moveFile(fileName : String, sourcePath: String, onResult: (success: Boolean, filePath: String) -> Unit)
}
