package com.jainakash.mywardrobe.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [WardrobeItemEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wardrobeItemDao(): WardrobeItemDao
}
