package com.jainakash.mywardrobe

import android.content.Context
import androidx.room.Room
import com.jainakash.mywardrobe.data.AppDatabase
import com.jainakash.mywardrobe.data.LocalImageStorage
import com.jainakash.mywardrobe.data.LocalWardrobeRepository
import com.jainakash.mywardrobe.data.WardrobeRepository

class AppContainer(context: Context) {
    private val database: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "my-wardrobe.db"
    ).build()

    val wardrobeRepository: WardrobeRepository =
        LocalWardrobeRepository(database.wardrobeItemDao())

    val imageStorage: LocalImageStorage =
        LocalImageStorage(context.filesDir)
}
