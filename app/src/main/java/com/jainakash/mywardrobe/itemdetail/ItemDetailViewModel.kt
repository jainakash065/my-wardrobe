package com.jainakash.mywardrobe.itemdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jainakash.mywardrobe.data.ImageStorage
import com.jainakash.mywardrobe.data.WardrobeRepository
import com.jainakash.mywardrobe.domain.WardrobeCategory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ItemDetailViewModel(
    private val repository: WardrobeRepository,
    private val imageStorage: ImageStorage,
    private val itemId: Long,
    initialState: ItemFormState = ItemFormState(),
    coroutineScope: CoroutineScope? = null,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {
    private val scope = coroutineScope ?: viewModelScope
    private val _formState = MutableStateFlow(initialState)
    val formState: StateFlow<ItemFormState> = _formState.asStateFlow()
    private var loadedItem = false

    init {
        scope.launch(dispatcher) {
            repository.observeItem(itemId).collect { item ->
                if (item != null && !loadedItem) {
                    _formState.value = item.toFormState()
                    loadedItem = true
                }
            }
        }
    }

    fun onNameChanged(name: String) {
        _formState.value = _formState.value.copy(name = name)
    }

    fun onCategoryChanged(category: WardrobeCategory) {
        _formState.value = _formState.value.copy(category = category)
    }

    fun onColorChanged(color: String) {
        _formState.value = _formState.value.copy(color = color)
    }

    fun onOccasionChanged(occasion: String) {
        _formState.value = _formState.value.copy(occasion = occasion)
    }

    fun onFabricChanged(fabric: String) {
        _formState.value = _formState.value.copy(fabric = fabric)
    }

    fun onSeasonChanged(season: String) {
        _formState.value = _formState.value.copy(season = season)
    }

    fun onNotesChanged(notes: String) {
        _formState.value = _formState.value.copy(notes = notes)
    }

    fun save(onSaved: () -> Unit) {
        val state = _formState.value
        if (!state.isValid) {
            return
        }

        scope.launch(dispatcher) {
            repository.updateItem(state.toWardrobeItem(itemId))
            onSaved()
        }
    }

    fun delete(onDeleted: () -> Unit) {
        val state = _formState.value
        scope.launch(dispatcher) {
            repository.deleteItem(state.toWardrobeItem(itemId))
            imageStorage.delete(state.photoPath)
            onDeleted()
        }
    }
}
