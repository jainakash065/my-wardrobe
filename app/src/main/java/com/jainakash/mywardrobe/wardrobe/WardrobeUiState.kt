package com.jainakash.mywardrobe.wardrobe

import com.jainakash.mywardrobe.domain.WardrobeCategory
import com.jainakash.mywardrobe.domain.WardrobeItem

data class WardrobeUiState(
    val query: String = "",
    val filters: WardrobeFilters = WardrobeFilters(),
    val items: List<WardrobeItem> = emptyList(),
    val reviewItemCount: Int = 0
) {
    val selectedCategory: WardrobeCategory?
        get() = filters.category

    val activeFilterLabels: List<String>
        get() = filters.activeLabels

    val hasActiveFilters: Boolean
        get() = filters.isActive
}
