package com.jainakash.mywardrobe.wardrobe

import com.jainakash.mywardrobe.data.WardrobeRepository
import com.jainakash.mywardrobe.domain.WardrobeCategory
import com.jainakash.mywardrobe.domain.WardrobeItem
import com.jainakash.mywardrobe.domain.WardrobeItemDraft
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WardrobeViewModelTest {
    @Test
    fun `query filters visible wardrobe items`() = runTest {
        val repository = FakeWardrobeRepository(
            listOf(
                WardrobeItem(1, "/blue.jpg", "Blue saree", WardrobeCategory.SAREE, "Blue", "Wedding", "Silk", "Festive", ""),
                WardrobeItem(2, "/black.jpg", "Black kurti", WardrobeCategory.KURTI, "Black", "Office", "Cotton", "Summer", "")
            )
        )
        val viewModel = WardrobeViewModel(
            repository = repository,
            coroutineScope = backgroundScope,
            dispatcher = UnconfinedTestDispatcher(testScheduler)
        )

        viewModel.onQueryChanged("office")

        assertEquals(listOf("Black kurti"), viewModel.uiState.value.items.map { it.name })
    }

    @Test
    fun `category selection filters visible wardrobe items`() = runTest {
        val repository = FakeWardrobeRepository(
            listOf(
                WardrobeItem(1, "/blue.jpg", "Blue saree", WardrobeCategory.SAREE, "Blue", "Wedding", "Silk", "Festive", ""),
                WardrobeItem(2, "/black.jpg", "Black kurti", WardrobeCategory.KURTI, "Black", "Office", "Cotton", "Summer", "")
            )
        )
        val viewModel = WardrobeViewModel(
            repository = repository,
            coroutineScope = backgroundScope,
            dispatcher = UnconfinedTestDispatcher(testScheduler)
        )

        viewModel.onCategorySelected(WardrobeCategory.SAREE)

        assertEquals(listOf("Blue saree"), viewModel.uiState.value.items.map { it.name })
    }

    @Test
    fun `review count includes incomplete items outside current filters`() = runTest {
        val repository = FakeWardrobeRepository(
            listOf(
                WardrobeItem(1, "/draft.jpg", "", WardrobeCategory.OTHER, "", "", "", "", ""),
                WardrobeItem(2, "/black.jpg", "Black kurti", WardrobeCategory.KURTI, "Black", "Office", "Cotton", "Summer", "")
            )
        )
        val viewModel = WardrobeViewModel(
            repository = repository,
            coroutineScope = backgroundScope,
            dispatcher = UnconfinedTestDispatcher(testScheduler)
        )

        viewModel.onQueryChanged("office")

        assertEquals(1, viewModel.uiState.value.reviewItemCount)
        assertEquals(listOf("Black kurti"), viewModel.uiState.value.items.map { it.name })
    }

    private class FakeWardrobeRepository(items: List<WardrobeItem>) : WardrobeRepository {
        private val state = MutableStateFlow(items)

        override fun observeItems(): Flow<List<WardrobeItem>> = state

        override fun observeItem(id: Long): Flow<WardrobeItem?> =
            MutableStateFlow(state.value.firstOrNull { it.id == id })

        override suspend fun saveDraft(draft: WardrobeItemDraft): Long = 0

        override suspend fun updateItem(item: WardrobeItem) = Unit

        override suspend fun deleteItem(item: WardrobeItem) = Unit
    }
}
