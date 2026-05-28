package com.jainakash.mywardrobe.itemdetail

import com.jainakash.mywardrobe.domain.WardrobeCategory
import com.jainakash.mywardrobe.domain.WardrobeItem
import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ItemFormStateTest {
    @Test
    fun `form is valid only when required fields are present`() {
        assertFalse(ItemFormState(photoPath = "/photo.jpg", name = "", color = "").isValid)
        assertFalse(ItemFormState(photoPath = "", name = "Blue saree", color = "Blue").isValid)
        assertTrue(ItemFormState(photoPath = "/photo.jpg", name = "Blue saree", color = "Blue").isValid)
    }

    @Test
    fun `form converts to wardrobe item draft`() {
        val state = ItemFormState(
            photoPath = "/photo.jpg",
            name = "Blue saree",
            category = WardrobeCategory.SAREE,
            color = "Blue",
            occasion = "Wedding",
            fabric = "Silk",
            season = "Festive",
            notes = "Gold border"
        )

        val draft = state.toDraft()

        assertEquals("/photo.jpg", draft.photoPath)
        assertEquals("Blue saree", draft.name)
        assertEquals(WardrobeCategory.SAREE, draft.category)
        assertEquals("Blue", draft.color)
        assertEquals("Wedding", draft.occasion)
        assertEquals("Silk", draft.fabric)
        assertEquals("Festive", draft.season)
        assertEquals("Gold border", draft.notes)
    }

    @Test
    fun `form converts to wardrobe item with id`() {
        val state = ItemFormState(
            photoPath = "/photo.jpg",
            name = "Black kurti",
            category = WardrobeCategory.KURTI,
            color = "Black"
        )

        val item = state.toWardrobeItem(id = 7)

        assertEquals(
            WardrobeItem(
                id = 7,
                photoPath = "/photo.jpg",
                name = "Black kurti",
                category = WardrobeCategory.KURTI,
                color = "Black",
                occasion = "",
                fabric = "",
                season = "",
                notes = ""
            ),
            item
        )
    }

    @Test
    fun `quick pick options include common wardrobe values`() {
        assertEquals(listOf("Black", "White", "Blue", "Pink", "Red", "Green", "Yellow", "Gold"), commonColors)
        assertEquals(listOf("Casual", "Office", "Party", "Wedding", "Festive", "Travel"), commonOccasions)
        assertEquals(listOf("Summer", "Winter", "Rainy", "All season", "Festive"), commonSeasons)
    }

    @Test
    fun `color chip styles keep light and dark swatches readable`() {
        assertEquals(Color.Black, styleForColorOption("Black").backgroundColor)
        assertEquals(Color.White, styleForColorOption("Black").contentColor)
        assertEquals(Color.White, styleForColorOption("White").backgroundColor)
        assertEquals(Color(0xFF242124), styleForColorOption("White").contentColor)
        assertEquals(Color(0xFFE2D8D0), styleForColorOption("White").borderColor)
        assertEquals(Color(0xFF2563EB), styleForColorOption("Blue").backgroundColor)
    }
}
