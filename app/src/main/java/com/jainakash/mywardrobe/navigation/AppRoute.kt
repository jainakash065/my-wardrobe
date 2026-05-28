package com.jainakash.mywardrobe.navigation

sealed class AppRoute(val route: String) {
    data object Launch : AppRoute("launch")
    data object Wardrobe : AppRoute("wardrobe")
    data object Capture : AppRoute("capture")
    data object ReviewQueue : AppRoute("review")
    data object ItemDetail : AppRoute("item/{itemId}") {
        fun create(itemId: Long): String = "item/$itemId"
    }
}

