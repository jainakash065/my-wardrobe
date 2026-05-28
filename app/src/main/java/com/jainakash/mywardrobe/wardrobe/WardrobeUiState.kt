package com.jainakash.mywardrobe.wardrobe

import com.jainakash.mywardrobe.domain.WardrobeCategory
import com.jainakash.mywardrobe.domain.WardrobeItem

data class WardrobeUiState(
    val query: String = "",
    val selectedCategory: WardrobeCategory? = null,
    val items: List<WardrobeItem> = emptyList(),
    val reviewItemCount: Int = 0
)
