package com.jainakash.mywardrobe.data

import com.jainakash.mywardrobe.domain.WardrobeItem
import com.jainakash.mywardrobe.domain.WardrobeItemDraft
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalWardrobeRepository(
    private val dao: WardrobeItemDao
) : WardrobeRepository {
    override fun observeItems(): Flow<List<WardrobeItem>> =
        dao.observeItems().map { entities -> entities.map { it.toDomain() } }

    override fun observeItem(id: Long): Flow<WardrobeItem?> =
        dao.observeItem(id).map { entity -> entity?.toDomain() }

    override suspend fun saveDraft(draft: WardrobeItemDraft): Long =
        dao.insert(draft.toEntity())

    override suspend fun updateItem(item: WardrobeItem) {
        dao.update(item.toEntity())
    }

    override suspend fun deleteItem(item: WardrobeItem) {
        dao.delete(item.toEntity())
    }
}
