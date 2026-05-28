package com.jainakash.mywardrobe.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wardrobe_items")
data class WardrobeItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val photoPath: String,
    val name: String,
    val category: String,
    val color: String,
    val occasion: String,
    val fabric: String,
    val season: String,
    val notes: String
)

