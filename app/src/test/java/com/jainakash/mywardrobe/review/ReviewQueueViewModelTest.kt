package com.jainakash.mywardrobe.review

import com.jainakash.mywardrobe.data.WardrobeRepository
import com.jainakash.mywardrobe.data.ImageStorage
import com.jainakash.mywardrobe.domain.WardrobeCategory
import com.jainakash.mywardrobe.domain.WardrobeItem
import com.jainakash.mywardrobe.domain.WardrobeItemDraft
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ReviewQueueViewModelTest {
    @Test
    fun `items missing required metadata need review`() {
        assertTrue(needsReview(WardrobeItem(1, "/a.jpg", "", WardrobeCategory.OTHER, "", "", "", "", "")))
        assertTrue(needsReview(WardrobeItem(2, "/b.jpg", "Blue saree", WardrobeCategory.SAREE, "", "", "", "", "")))
        assertFalse(needsReview(WardrobeItem(3, "/c.jpg", "Blue saree", WardrobeCategory.SAREE, "Blue", "", "", "", "")))
    }

    @Test
    fun `view model exposes only items needing review`() = runTest {
        val repository = FakeWardrobeRepository(
            listOf(
                WardrobeItem(1, "/a.jpg", "", WardrobeCategory.OTHER, "", "", "", "", ""),
                WardrobeItem(2, "/b.jpg", "Blue saree", WardrobeCategory.SAREE, "Blue", "", "", "", "")
            )
        )

        val viewModel = ReviewQueueViewModel(
            repository = repository,
            coroutineScope = backgroundScope,
            dispatcher = UnconfinedTestDispatcher(testScheduler)
        )

        assertEquals(listOf(1L), viewModel.uiState.value.items.map { it.id })
    }

    @Test
    fun `delete removes item and stored photo`() = runTest {
        val item = WardrobeItem(1, "/a.jpg", "", WardrobeCategory.OTHER, "", "", "", "", "")
        val repository = FakeWardrobeRepository(listOf(item))
        val imageStorage = FakeImageStorage()
        val viewModel = ReviewQueueViewModel(
            repository = repository,
            imageStorage = imageStorage,
            coroutineScope = backgroundScope,
            dispatcher = UnconfinedTestDispatcher(testScheduler)
        )

        viewModel.delete(item)

        assertEquals(emptyList<WardrobeItem>(), repository.currentItems)
        assertEquals(listOf("/a.jpg"), imageStorage.deletedPaths)
    }

    private class FakeWardrobeRepository(items: List<WardrobeItem>) : WardrobeRepository {
        private val state = MutableStateFlow(items)

        override fun observeItems(): Flow<List<WardrobeItem>> = state

        override fun observeItem(id: Long): Flow<WardrobeItem?> =
            MutableStateFlow(state.value.firstOrNull { it.id == id })

        override suspend fun saveDraft(draft: WardrobeItemDraft): Long = 0

        override suspend fun updateItem(item: WardrobeItem) = Unit

        override suspend fun deleteItem(item: WardrobeItem) {
            state.value = state.value.filterNot { it.id == item.id }
        }

        val currentItems: List<WardrobeItem>
            get() = state.value
    }

    private class FakeImageStorage : ImageStorage {
        val deletedPaths = mutableListOf<String>()

        override fun saveBytes(displayName: String, bytes: ByteArray): String = "/unused.jpg"

        override fun delete(path: String): Boolean {
            deletedPaths += path
            return true
        }
    }
}
