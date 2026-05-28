package com.jainakash.mywardrobe

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jainakash.mywardrobe.data.AppDatabase
import com.jainakash.mywardrobe.data.LocalImageStorage
import com.jainakash.mywardrobe.data.LocalWardrobeRepository
import com.jainakash.mywardrobe.data.WardrobeRepository

class AppContainer(context: Context) {
    private val database: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "my-wardrobe.db"
    ).addMigrations(MIGRATION_1_2).build()

    val wardrobeRepository: WardrobeRepository =
        LocalWardrobeRepository(database.wardrobeItemDao())

    val imageStorage: LocalImageStorage =
        LocalImageStorage(context.filesDir)

    private companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE wardrobe_items ADD COLUMN isFavorite INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}
