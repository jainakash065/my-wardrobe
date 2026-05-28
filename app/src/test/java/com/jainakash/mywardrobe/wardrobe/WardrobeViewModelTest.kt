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

    @Test
    fun `dashboard state exposes total count recent items and category counts`() = runTest {
        val repository = FakeWardrobeRepository(
            listOf(
                WardrobeItem(1, "/blue.jpg", "Blue saree", WardrobeCategory.SAREE, "Blue", "Wedding", "Silk", "Festive", ""),
                WardrobeItem(2, "/black.jpg", "Black kurti", WardrobeCategory.KURTI, "Black", "Office", "Cotton", "Summer", ""),
                WardrobeItem(3, "/pink.jpg", "Pink dress", WardrobeCategory.DRESS, "Pink", "Party", "", "Winter", ""),
                WardrobeItem(4, "/white.jpg", "White top", WardrobeCategory.TOP, "White", "Casual", "", "Summer", ""),
                WardrobeItem(5, "/gold.jpg", "Gold dupatta", WardrobeCategory.DUPATTA, "Gold", "Festive", "", "Festive", "")
            )
        )
        val viewModel = WardrobeViewModel(
            repository = repository,
            coroutineScope = backgroundScope,
            dispatcher = UnconfinedTestDispatcher(testScheduler)
        )

        val state = viewModel.uiState.value

        assertEquals(5, state.totalItemCount)
        assertEquals(listOf("Gold dupatta", "White top", "Pink dress", "Black kurti"), state.recentItems.map { it.name })
        assertEquals(1, state.categorySummaries.first { it.category == WardrobeCategory.SAREE }.count)
        assertEquals(1, state.categorySummaries.first { it.category == WardrobeCategory.KURTI }.count)
    }

    @Test
    fun `advanced filters combine with search query`() = runTest {
        val repository = FakeWardrobeRepository(
            listOf(
                WardrobeItem(1, "/blue.jpg", "Blue silk saree", WardrobeCategory.SAREE, "Blue", "Wedding", "Silk", "Festive", ""),
                WardrobeItem(2, "/black.jpg", "Black cotton kurti", WardrobeCategory.KURTI, "Black", "Office", "Cotton", "Summer", ""),
                WardrobeItem(3, "/pink.jpg", "Pink silk kurti", WardrobeCategory.KURTI, "Pink", "Party", "Silk", "Winter", "")
            )
        )
        val viewModel = WardrobeViewModel(
            repository = repository,
            coroutineScope = backgroundScope,
            dispatcher = UnconfinedTestDispatcher(testScheduler)
        )

        viewModel.onQueryChanged("silk")
        viewModel.onFilterChanged(WardrobeFilters(color = "Pink", occasion = "Party"))

        assertEquals(listOf("Pink silk kurti"), viewModel.uiState.value.items.map { it.name })
        assertEquals(listOf("Color: Pink", "Occasion: Party"), viewModel.uiState.value.activeFilterLabels)
    }

    @Test
    fun `removing and clearing advanced filters updates visible items`() = runTest {
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

        viewModel.onFilterChanged(WardrobeFilters(color = "Blue", season = "Festive"))
        viewModel.clearColorFilter()

        assertEquals(listOf("Blue saree"), viewModel.uiState.value.items.map { it.name })
        assertEquals(listOf("Season: Festive"), viewModel.uiState.value.activeFilterLabels)

        viewModel.clearFilters()

        assertEquals(listOf("Blue saree", "Black kurti"), viewModel.uiState.value.items.map { it.name })
        assertEquals(emptyList<String>(), viewModel.uiState.value.activeFilterLabels)
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
