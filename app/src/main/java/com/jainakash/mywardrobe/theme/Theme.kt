package com.jainakash.mywardrobe.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = WardrobeRose,
    secondary = WardrobeTeal,
    background = WardrobeMist,
    surface = WardrobeMist,
    onPrimary = WardrobeMist,
    onSecondary = WardrobeMist,
    onBackground = WardrobeInk,
    onSurface = WardrobeInk
)

@Composable
fun MyWardrobeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = MyWardrobeTypography,
        content = content
    )
}

