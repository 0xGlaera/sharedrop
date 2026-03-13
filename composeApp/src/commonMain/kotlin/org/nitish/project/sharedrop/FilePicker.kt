package org.nitish.project.sharedrop

expect class FilePicker() {
    fun pickFile(onFilePicked: (fileName: String, bytes: ByteArray) -> Unit)
}
