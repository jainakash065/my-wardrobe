package com.jainakash.mywardrobe.data

import com.jainakash.mywardrobe.domain.WardrobeItem
import com.jainakash.mywardrobe.domain.WardrobeItemDraft
import kotlinx.coroutines.flow.Flow

interface WardrobeRepository {
    fun observeItems(): Flow<List<WardrobeItem>>
    fun observeItem(id: Long): Flow<WardrobeItem?>
    suspend fun saveDraft(draft: WardrobeItemDraft): Long
    suspend fun updateItem(item: WardrobeItem)
    suspend fun deleteItem(item: WardrobeItem)
}

