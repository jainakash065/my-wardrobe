package com.jainakash.mywardrobe.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jainakash.mywardrobe.launch.LaunchScreen

@Composable
fun MyWardrobeNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoute.Launch.route
    ) {
        composable(AppRoute.Launch.route) {
            LaunchScreen(
                onFinished = {
                    navController.navigate(AppRoute.Wardrobe.route) {
                        popUpTo(AppRoute.Launch.route) { inclusive = true }
                    }
                }
            )
        }
        composable(AppRoute.Wardrobe.route) {
            TemporaryScreen("Wardrobe")
        }
        composable(AppRoute.Capture.route) {
            TemporaryScreen("Capture")
        }
        composable(AppRoute.ReviewQueue.route) {
            TemporaryScreen("Review Queue")
        }
        composable(AppRoute.ItemDetail.route) {
            TemporaryScreen("Item Detail")
        }
    }
}

@Composable
private fun TemporaryScreen(title: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

