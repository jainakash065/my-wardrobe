package com.jainakash.mywardrobe.itemdetail

import androidx.compose.ui.graphics.Color

val commonColors = listOf("Black", "White", "Blue", "Pink", "Red", "Green", "Yellow", "Gold")

val commonOccasions = listOf("Casual", "Office", "Party", "Wedding", "Festive", "Travel")

val commonSeasons = listOf("Summer", "Winter", "Rainy", "All season", "Festive")

data class ColorChipStyle(
    val backgroundColor: Color,
    val contentColor: Color,
    val borderColor: Color
)

fun styleForColorOption(option: String): ColorChipStyle =
    when (option.trim().lowercase()) {
        "black" -> ColorChipStyle(
            backgroundColor = Color.Black,
            contentColor = Color.White,
            borderColor = Color.Black
        )
        "white" -> ColorChipStyle(
            backgroundColor = Color.White,
            contentColor = Color(0xFF242124),
            borderColor = Color(0xFFE2D8D0)
        )
        "blue" -> readableStyle(Color(0xFF2563EB), Color.White)
        "pink" -> readableStyle(Color(0xFFF472B6), Color(0xFF242124))
        "red" -> readableStyle(Color(0xFFDC2626), Color.White)
        "green" -> readableStyle(Color(0xFF16A34A), Color.White)
        "yellow" -> readableStyle(Color(0xFFFACC15), Color(0xFF242124))
        "gold" -> readableStyle(Color(0xFFD4A017), Color(0xFF242124))
        else -> readableStyle(Color(0xFFF7F3F0), Color(0xFF242124))
    }

private fun readableStyle(backgroundColor: Color, contentColor: Color): ColorChipStyle =
    ColorChipStyle(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        borderColor = backgroundColor
    )
