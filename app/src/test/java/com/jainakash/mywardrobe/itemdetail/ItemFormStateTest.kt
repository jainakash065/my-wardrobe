package com.jainakash.mywardrobe.itemdetail

import com.jainakash.mywardrobe.domain.WardrobeCategory
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
}
