package com.jainakash.mywardrobe.data

interface ImageStorage {
    fun saveBytes(displayName: String, bytes: ByteArray): String
}

