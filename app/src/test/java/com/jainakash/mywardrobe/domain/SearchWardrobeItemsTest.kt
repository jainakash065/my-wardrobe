package com.jainakash.mywardrobe.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class SearchWardrobeItemsTest {
    private val items = listOf(
        WardrobeItem(
            id = 1,
            photoPath = "/photos/blue-saree.jpg",
            name = "Blue silk saree",
            category = WardrobeCategory.SAREE,
            color = "Blue",
            occasion = "Wedding",
            fabric = "Silk",
            season = "Festive",
            notes = "Gold border"
        ),
        WardrobeItem(
            id = 2,
            photoPath = "/photos/black-kurti.jpg",
            name = "Black kurti",
            category = WardrobeCategory.KURTI,
            color = "Black",
            occasion = "Office",
            fabric = "Cotton",
            season = "Summer",
            notes = ""
        )
    )

    @Test
    fun `blank query returns all items`() {
        assertEquals(items, SearchWardrobeItems.apply(items, " "))
    }

    @Test
    fun `query matches name category color occasion fabric season and notes`() {
        assertEquals(listOf(items[0]), SearchWardrobeItems.apply(items, "gold"))
        assertEquals(listOf(items[0]), SearchWardrobeItems.apply(items, "saree"))
        assertEquals(listOf(items[1]), SearchWardrobeItems.apply(items, "office"))
        assertEquals(listOf(items[1]), SearchWardrobeItems.apply(items, "cotton"))
    }
}
