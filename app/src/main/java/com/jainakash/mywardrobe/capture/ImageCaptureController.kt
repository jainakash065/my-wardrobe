package com.jainakash.mywardrobe.capture

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.core.content.FileProvider
import com.jainakash.mywardrobe.data.ImageStorage
import java.io.File
import java.util.UUID

class ImageCaptureController(
    private val context: Context,
    private val contentResolver: ContentResolver,
    private val imageStorage: ImageStorage
) {
    fun createCameraImageUri(): Uri {
        val directory = File(context.cacheDir, CAMERA_DIRECTORY)
        check(directory.exists() || directory.mkdirs()) {
            "Unable to create camera image directory"
        }

        val file = File(directory, "${UUID.randomUUID()}.jpg")
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    fun saveCameraImage(uri: Uri): String = saveUriImage(uri, CAMERA_IMAGE_NAME)

    fun saveGalleryImage(uri: Uri): String {
        val displayName = displayNameFor(uri)
        return saveUriImage(uri, displayName)
    }

    private fun saveUriImage(uri: Uri, displayName: String): String {
        val bytes = contentResolver.openInputStream(uri)?.use { inputStream ->
            inputStream.readBytes()
        } ?: error("Unable to open selected wardrobe image")

        return imageStorage.saveBytes(displayName, bytes)
    }

    private fun displayNameFor(uri: Uri): String {
        val cursor = contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index >= 0) {
                    return it.getString(index)
                }
            }
        }

        return uri.lastPathSegment ?: DEFAULT_IMAGE_NAME
    }

    private companion object {
        const val CAMERA_DIRECTORY = "camera"
        const val CAMERA_IMAGE_NAME = "camera-photo.jpg"
        const val DEFAULT_IMAGE_NAME = "wardrobe-photo.jpg"
    }
}
