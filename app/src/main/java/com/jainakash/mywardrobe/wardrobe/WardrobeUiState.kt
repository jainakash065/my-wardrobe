package com.jainakash.mywardrobe.wardrobe

import com.jainakash.mywardrobe.domain.WardrobeCategory
import com.jainakash.mywardrobe.domain.WardrobeItem

data class WardrobeUiState(
    val query: String = "",
    val filters: WardrobeFilters = WardrobeFilters(),
    val items: List<WardrobeItem> = emptyList(),
    val totalItemCount: Int = 0,
    val recentItems: List<WardrobeItem> = emptyList(),
    val categorySummaries: List<CategorySummary> = emptyList(),
    val reviewItemCount: Int = 0
) {
    val selectedCategory: WardrobeCategory?
        get() = filters.category

    val activeFilterLabels: List<String>
        get() = filters.activeLabels

    val hasActiveFilters: Boolean
        get() = filters.isActive
}

data class CategorySummary(
    val category: WardrobeCategory,
    val count: Int
)
