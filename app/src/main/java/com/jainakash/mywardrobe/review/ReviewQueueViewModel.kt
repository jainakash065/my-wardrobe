package com.jainakash.mywardrobe.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jainakash.mywardrobe.data.ImageStorage
import com.jainakash.mywardrobe.data.WardrobeRepository
import com.jainakash.mywardrobe.domain.WardrobeItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

fun needsReview(item: WardrobeItem): Boolean =
    item.name.isBlank() || item.color.isBlank()

class ReviewQueueViewModel(
    private val repository: WardrobeRepository,
    private val imageStorage: ImageStorage? = null,
    coroutineScope: CoroutineScope? = null,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {
    private val scope = coroutineScope ?: viewModelScope
    private val _uiState = MutableStateFlow(ReviewQueueUiState())
    val uiState: StateFlow<ReviewQueueUiState> = _uiState.asStateFlow()

    init {
        scope.launch(dispatcher) {
            repository.observeItems().collect { items ->
                _uiState.value = ReviewQueueUiState(items = items.filter(::needsReview))
            }
        }
    }

    fun delete(item: WardrobeItem, onDeleted: () -> Unit = {}) {
        scope.launch(dispatcher) {
            repository.deleteItem(item)
            imageStorage?.delete(item.photoPath)
            onDeleted()
        }
    }
}
