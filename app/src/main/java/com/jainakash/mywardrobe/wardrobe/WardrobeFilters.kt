package com.jainakash.mywardrobe.wardrobe

import com.jainakash.mywardrobe.domain.WardrobeCategory

data class WardrobeFilters(
    val category: WardrobeCategory? = null,
    val color: String = "",
    val occasion: String = "",
    val fabric: String = "",
    val season: String = ""
) {
    val isActive: Boolean
        get() = category != null ||
            color.isNotBlank() ||
            occasion.isNotBlank() ||
            fabric.isNotBlank() ||
            season.isNotBlank()

    val activeLabels: List<String>
        get() = buildList {
            category?.let { add("Category: ${it.displayName}") }
            if (color.isNotBlank()) add("Color: $color")
            if (occasion.isNotBlank()) add("Occasion: $occasion")
            if (fabric.isNotBlank()) add("Fabric: $fabric")
            if (season.isNotBlank()) add("Season: $season")
        }
}
