package org.nitish.project.sharedrop

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        AndroidContext.context = this  // for file picker
        AndroidContext.appContext = applicationContext  // for NsdManager
        setContent {
            App()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AndroidContext.FILE_PICK_REQUEST_CODE && resultCode == RESULT_OK) {
            val uri = data?.data ?: return
            val fileName = getFileName(uri)
            val bytes = contentResolver.openInputStream(uri)?.readBytes() ?: return
            AndroidContext.onFilePicked?.invoke(fileName, bytes)
            AndroidContext.onFilePicked = null
        }
    }

    private fun getFileName(uri: android.net.Uri): String {
        var name = "unknown_file"
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val index = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                if (index != -1) name = it.getString(index)
            }
        }
        return name
    }

    override fun onDestroy() {
        super.onDestroy()
        AndroidContext.context = null
    }
}