package com.jainakash.mywardrobe.domain

object SearchWardrobeItems {
    fun apply(items: List<WardrobeItem>, query: String): List<WardrobeItem> {
        val normalizedQuery = query.trim().lowercase()
        if (normalizedQuery.isEmpty()) {
            return items
        }

        return items.filter { item ->
            listOf(
                item.name,
                item.category.displayName,
                item.color,
                item.occasion,
                item.fabric,
                item.season,
                item.notes
            ).any { value -> value.lowercase().contains(normalizedQuery) }
        }
    }
}
