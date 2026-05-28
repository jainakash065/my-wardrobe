package com.jainakash.mywardrobe.itemdetail

import androidx.lifecycle.ViewModel
import com.jainakash.mywardrobe.domain.WardrobeCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ItemDetailViewModel(
    initialState: ItemFormState = ItemFormState()
) : ViewModel() {
    private val _formState = MutableStateFlow(initialState)
    val formState: StateFlow<ItemFormState> = _formState.asStateFlow()

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
}

