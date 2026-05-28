package com.jainakash.mywardrobe.data

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import kotlin.io.path.createTempDirectory

class LocalImageStorageTest {
    @Test
    fun `stores bytes as private wardrobe image file`() {
        val root = createTempDirectory().toFile()
        val storage = LocalImageStorage(root)
        val bytes = byteArrayOf(1, 2, 3)

        val path = storage.saveBytes("photo.jpg", bytes)

        val saved = File(path)
        assertTrue(saved.exists())
        assertEquals("wardrobe-images", saved.parentFile?.name)
        assertArrayEquals(bytes, saved.readBytes())
    }

    @Test
    fun `deletes stored wardrobe image file`() {
        val root = createTempDirectory().toFile()
        val storage = LocalImageStorage(root)
        val path = storage.saveBytes("photo.jpg", byteArrayOf(1, 2, 3))

        assertTrue(storage.delete(path))

        assertFalse(File(path).exists())
    }
}
