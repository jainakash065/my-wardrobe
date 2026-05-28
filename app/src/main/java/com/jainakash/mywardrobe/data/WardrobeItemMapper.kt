package com.jainakash.mywardrobe.data

import com.jainakash.mywardrobe.domain.WardrobeCategory
import com.jainakash.mywardrobe.domain.WardrobeItem
import com.jainakash.mywardrobe.domain.WardrobeItemDraft

fun WardrobeItem.toEntity(): WardrobeItemEntity = WardrobeItemEntity(
    id = id,
    photoPath = photoPath,
    name = name,
    category = category.name,
    color = color,
    occasion = occasion,
    fabric = fabric,
    season = season,
    notes = notes
)

fun WardrobeItemDraft.toEntity(): WardrobeItemEntity = WardrobeItemEntity(
    photoPath = photoPath,
    name = name,
    category = category.name,
    color = color,
    occasion = occasion,
    fabric = fabric,
    season = season,
    notes = notes
)

fun WardrobeItemEntity.toDomain(): WardrobeItem = WardrobeItem(
    id = id,
    photoPath = photoPath,
    name = name,
    category = WardrobeCategory.valueOf(category),
    color = color,
    occasion = occasion,
    fabric = fabric,
    season = season,
    notes = notes
)

