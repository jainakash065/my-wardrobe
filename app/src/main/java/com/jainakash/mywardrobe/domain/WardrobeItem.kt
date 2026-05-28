package com.jainakash.mywardrobe.domain

data class WardrobeItem(
    val id: Long,
    val photoPath: String,
    val name: String,
    val category: WardrobeCategory,
    val color: String,
    val occasion: String,
    val fabric: String,
    val season: String,
    val notes: String
)

