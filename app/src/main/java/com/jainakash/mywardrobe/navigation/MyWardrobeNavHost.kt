package com.jainakash.mywardrobe.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jainakash.mywardrobe.AppContainer
import com.jainakash.mywardrobe.launch.LaunchScreen
import com.jainakash.mywardrobe.wardrobe.WardrobeScreen
import com.jainakash.mywardrobe.wardrobe.WardrobeViewModel

@Composable
fun MyWardrobeNavHost(appContainer: AppContainer) {
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
            val viewModel = remember {
                WardrobeViewModel(appContainer.wardrobeRepository)
            }
            val state by viewModel.uiState.collectAsState()

            WardrobeScreen(
                state = state,
                onQueryChanged = viewModel::onQueryChanged,
                onCategorySelected = viewModel::onCategorySelected,
                onAddClicked = { navController.navigate(AppRoute.Capture.route) },
                onItemClicked = { itemId -> navController.navigate(AppRoute.ItemDetail.create(itemId)) }
            )
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
