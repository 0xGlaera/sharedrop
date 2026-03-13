package org.nitish.project.sharedrop

actual class FilePicker {
    actual fun pickFile(onFilePicked: (fileName: String, bytes: ByteArray) -> Unit) {
    }
}
