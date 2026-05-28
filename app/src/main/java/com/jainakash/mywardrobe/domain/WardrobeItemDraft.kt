package com.jainakash.mywardrobe.domain

data class WardrobeItemDraft(
    val photoPath: String,
    val name: String = "",
    val category: WardrobeCategory = WardrobeCategory.OTHER,
    val color: String = "",
    val occasion: String = "",
    val fabric: String = "",
    val season: String = "",
    val notes: String = ""
) {
    val hasRequiredFields: Boolean
        get() = photoPath.isNotBlank() && name.isNotBlank() && color.isNotBlank()
}

