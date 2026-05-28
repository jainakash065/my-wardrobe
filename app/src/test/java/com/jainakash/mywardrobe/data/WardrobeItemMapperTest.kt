package com.jainakash.mywardrobe.data

import com.jainakash.mywardrobe.domain.WardrobeCategory
import com.jainakash.mywardrobe.domain.WardrobeItem
import org.junit.Assert.assertEquals
import org.junit.Test

class WardrobeItemMapperTest {
    @Test
    fun `maps domain item to entity and back`() {
        val item = WardrobeItem(
            id = 42,
            photoPath = "/files/photo.jpg",
            name = "Gold dupatta",
            category = WardrobeCategory.DUPATTA,
            color = "Gold",
            occasion = "Wedding",
            fabric = "Silk",
            season = "Festive",
            notes = "Pair with blue saree",
            isFavorite = true
        )

        val entity = item.toEntity()

        assertEquals(item, entity.toDomain())
    }
}
