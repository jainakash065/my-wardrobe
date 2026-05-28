package com.jainakash.mywardrobe.capture

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import com.jainakash.mywardrobe.data.ImageStorage

class ImageCaptureController(
    private val contentResolver: ContentResolver,
    private val imageStorage: ImageStorage
) {
    fun saveGalleryImage(uri: Uri): String {
        val displayName = displayNameFor(uri)
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
        const val DEFAULT_IMAGE_NAME = "wardrobe-photo.jpg"
    }
}

