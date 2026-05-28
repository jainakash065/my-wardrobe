package com.jainakash.mywardrobe.wardrobe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jainakash.mywardrobe.data.WardrobeRepository
import com.jainakash.mywardrobe.domain.SearchWardrobeItems
import com.jainakash.mywardrobe.domain.WardrobeCategory
import com.jainakash.mywardrobe.domain.WardrobeItem
import com.jainakash.mywardrobe.review.needsReview
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WardrobeViewModel(
    private val repository: WardrobeRepository,
    coroutineScope: CoroutineScope? = null,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {
    private val scope = coroutineScope ?: viewModelScope
    private val allItems = mutableListOf<WardrobeItem>()
    private val _uiState = MutableStateFlow(WardrobeUiState())
    val uiState: StateFlow<WardrobeUiState> = _uiState.asStateFlow()

    init {
        scope.launch(dispatcher) {
            repository.observeItems().collect { items ->
                allItems.clear()
                allItems.addAll(items)
                applyFilters()
            }
        }
    }

    fun onQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
        applyFilters()
    }

    fun onCategorySelected(category: WardrobeCategory?) {
        _uiState.value = _uiState.value.copy(filters = _uiState.value.filters.copy(category = category))
        applyFilters()
    }

    fun onFilterChanged(filters: WardrobeFilters) {
        _uiState.value = _uiState.value.copy(filters = filters)
        applyFilters()
    }

    fun clearColorFilter() {
        _uiState.value = _uiState.value.copy(filters = _uiState.value.filters.copy(color = ""))
        applyFilters()
    }

    fun clearOccasionFilter() {
        _uiState.value = _uiState.value.copy(filters = _uiState.value.filters.copy(occasion = ""))
        applyFilters()
    }

    fun clearSeasonFilter() {
        _uiState.value = _uiState.value.copy(filters = _uiState.value.filters.copy(season = ""))
        applyFilters()
    }

    fun clearCategoryFilter() {
        _uiState.value = _uiState.value.copy(filters = _uiState.value.filters.copy(category = null))
        applyFilters()
    }

    fun clearFilters() {
        _uiState.value = _uiState.value.copy(filters = WardrobeFilters())
        applyFilters()
    }

    private fun applyFilters() {
        val state = _uiState.value
        val filters = state.filters
        val searched = SearchWardrobeItems.apply(allItems, state.query)
        val filtered = searched
            .filter { item -> filters.category == null || item.category == filters.category }
            .filter { item -> filters.color.isBlank() || item.color.equals(filters.color, ignoreCase = true) }
            .filter { item -> filters.occasion.isBlank() || item.occasion.equals(filters.occasion, ignoreCase = true) }
            .filter { item -> filters.fabric.isBlank() || item.fabric.equals(filters.fabric, ignoreCase = true) }
            .filter { item -> filters.season.isBlank() || item.season.equals(filters.season, ignoreCase = true) }
        _uiState.value = state.copy(
            items = filtered,
            totalItemCount = allItems.size,
            recentItems = allItems.sortedByDescending { it.id }.take(4),
            categorySummaries = categorySummaries(),
            reviewItemCount = allItems.count(::needsReview)
        )
    }

    private fun categorySummaries(): List<CategorySummary> =
        WardrobeCategory.entries.mapNotNull { category ->
            val count = allItems.count { it.category == category }
            if (count == 0) {
                null
            } else {
                CategorySummary(
                    category = category,
                    count = count,
                    previewPhotoPath = allItems
                        .filter { it.category == category }
                        .maxByOrNull { it.id }
                        ?.photoPath
                        .orEmpty()
                )
            }
        }
}
