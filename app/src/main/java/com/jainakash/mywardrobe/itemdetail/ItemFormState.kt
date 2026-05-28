package com.jainakash.mywardrobe.itemdetail

import com.jainakash.mywardrobe.domain.WardrobeCategory
import com.jainakash.mywardrobe.domain.WardrobeItem
import com.jainakash.mywardrobe.domain.WardrobeItemDraft

data class ItemFormState(
    val photoPath: String = "",
    val name: String = "",
    val category: WardrobeCategory = WardrobeCategory.OTHER,
    val color: String = "",
    val occasion: String = "",
    val fabric: String = "",
    val season: String = "",
    val notes: String = ""
) {
    val isValid: Boolean
        get() = photoPath.isNotBlank() && name.isNotBlank() && color.isNotBlank()

    fun toDraft(): WardrobeItemDraft = WardrobeItemDraft(
        photoPath = photoPath,
        name = name,
        category = category,
        color = color,
        occasion = occasion,
        fabric = fabric,
        season = season,
        notes = notes
    )
}

fun WardrobeItem.toFormState(): ItemFormState = ItemFormState(
    photoPath = photoPath,
    name = name,
    category = category,
    color = color,
    occasion = occasion,
    fabric = fabric,
    season = season,
    notes = notes
)

fun ItemFormState.toWardrobeItem(id: Long): WardrobeItem = WardrobeItem(
    id = id,
    photoPath = photoPath,
    name = name,
    category = category,
    color = color,
    occasion = occasion,
    fabric = fabric,
    season = season,
    notes = notes
)
