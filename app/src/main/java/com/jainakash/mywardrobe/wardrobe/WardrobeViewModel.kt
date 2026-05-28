package com.jainakash.mywardrobe.wardrobe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jainakash.mywardrobe.data.WardrobeRepository
import com.jainakash.mywardrobe.domain.SearchWardrobeItems
import com.jainakash.mywardrobe.domain.WardrobeCategory
import com.jainakash.mywardrobe.domain.WardrobeItem
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
        _uiState.value = _uiState.value.copy(selectedCategory = category)
        applyFilters()
    }

    private fun applyFilters() {
        val state = _uiState.value
        val searched = SearchWardrobeItems.apply(allItems, state.query)
        val filtered = state.selectedCategory?.let { category ->
            searched.filter { it.category == category }
        } ?: searched
        _uiState.value = state.copy(items = filtered)
    }
}

