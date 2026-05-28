package com.jainakash.mywardrobe.review

import com.jainakash.mywardrobe.domain.WardrobeItem

data class ReviewQueueUiState(
    val items: List<WardrobeItem> = emptyList()
)

