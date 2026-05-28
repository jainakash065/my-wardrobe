package com.jainakash.mywardrobe.data

import java.io.File
import java.util.UUID

class LocalImageStorage(
    private val filesDir: File
) : ImageStorage {
    override fun saveBytes(displayName: String, bytes: ByteArray): String {
        val directory = File(filesDir, IMAGE_DIRECTORY)
        check(directory.exists() || directory.mkdirs()) {
            "Unable to create wardrobe image directory"
        }

        val file = File(directory, "${UUID.randomUUID()}.${extensionFor(displayName)}")
        file.writeBytes(bytes)
        return file.absolutePath
    }

    private fun extensionFor(displayName: String): String {
        val extension = displayName.substringAfterLast('.', missingDelimiterValue = "jpg")
        return extension.takeIf { it.isNotBlank() && it != displayName } ?: "jpg"
    }

    private companion object {
        const val IMAGE_DIRECTORY = "wardrobe-images"
    }
}
