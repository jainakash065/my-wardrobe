package com.jainakash.mywardrobe.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WardrobeItemDao {
    @Query("SELECT * FROM wardrobe_items ORDER BY name COLLATE NOCASE ASC")
    fun observeItems(): Flow<List<WardrobeItemEntity>>

    @Query("SELECT * FROM wardrobe_items WHERE id = :id")
    fun observeItem(id: Long): Flow<WardrobeItemEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: WardrobeItemEntity): Long

    @Update
    suspend fun update(item: WardrobeItemEntity)

    @Delete
    suspend fun delete(item: WardrobeItemEntity)
}

