package com.cinescope.cinescope.presentation.components

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

@Composable
actual fun rememberImagePickerLauncher(
    onImageSelected: (String?) -> Unit
): ImagePickerLauncher {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val uri = result.data?.data
        if (uri != null) {
            val savedPath = saveImageToInternalStorage(context, uri)
            onImageSelected(savedPath)
        } else {
            onImageSelected(null)
        }
    }

    return remember {
        object : ImagePickerLauncher {
            override fun launch() {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                    type = "image/*"
                }
                launcher.launch(intent)
            }
        }
    }
}

private fun saveImageToInternalStorage(context: android.content.Context, uri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null

        val profileDir = File(context.filesDir, "profile_images")

        if (!profileDir.exists()) {
            profileDir.mkdirs()
        }

        val filename = "profile_${UUID.randomUUID()}.jpg"
        val outputFile = File(profileDir, filename)

        FileOutputStream(outputFile).use { outputStream ->
            inputStream.copyTo(outputStream)
        }
        inputStream.close()

        outputFile.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()

        null
    }
}
