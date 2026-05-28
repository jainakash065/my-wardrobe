package com.jainakash.mywardrobe.wardrobe

import com.jainakash.mywardrobe.domain.WardrobeCategory

data class WardrobeFilters(
    val category: WardrobeCategory? = null,
    val color: String = "",
    val occasion: String = "",
    val fabric: String = "",
    val season: String = ""
)

