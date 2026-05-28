package com.jainakash.mywardrobe.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import com.jainakash.mywardrobe.AppContainer
import com.jainakash.mywardrobe.capture.CaptureScreen
import com.jainakash.mywardrobe.capture.CaptureViewModel
import com.jainakash.mywardrobe.capture.ImageCaptureController
import com.jainakash.mywardrobe.itemdetail.ItemDetailScreen
import com.jainakash.mywardrobe.itemdetail.ItemDetailViewModel
import com.jainakash.mywardrobe.launch.LaunchScreen
import com.jainakash.mywardrobe.review.ReviewQueueScreen
import com.jainakash.mywardrobe.review.ReviewQueueViewModel
import com.jainakash.mywardrobe.wardrobe.WardrobeAllItemsScreen
import com.jainakash.mywardrobe.wardrobe.WardrobeHomeScreen
import com.jainakash.mywardrobe.wardrobe.WardrobeViewModel

@Composable
fun MyWardrobeNavHost(appContainer: AppContainer) {
    val navController = rememberNavController()
    val wardrobeViewModel = remember {
        WardrobeViewModel(appContainer.wardrobeRepository)
    }

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
            val state by wardrobeViewModel.uiState.collectAsState()

            WardrobeHomeScreen(
                state = state,
                onQueryChanged = wardrobeViewModel::onQueryChanged,
                onCategorySelected = wardrobeViewModel::onCategorySelected,
                onFiltersChanged = wardrobeViewModel::onFilterChanged,
                onClearCategoryFilter = wardrobeViewModel::clearCategoryFilter,
                onClearColorFilter = wardrobeViewModel::clearColorFilter,
                onClearOccasionFilter = wardrobeViewModel::clearOccasionFilter,
                onClearSeasonFilter = wardrobeViewModel::clearSeasonFilter,
                onClearFilters = wardrobeViewModel::clearFilters,
                onAddClicked = { navController.navigate(AppRoute.Capture.route) },
                onReviewClicked = { navController.navigate(AppRoute.ReviewQueue.route) },
                onViewAllClicked = { navController.navigate(AppRoute.AllItems.route) },
                onItemClicked = { itemId -> navController.navigate(AppRoute.ItemDetail.create(itemId)) }
            )
        }
        composable(AppRoute.AllItems.route) {
            val state by wardrobeViewModel.uiState.collectAsState()

            WardrobeAllItemsScreen(
                state = state,
                onQueryChanged = wardrobeViewModel::onQueryChanged,
                onCategorySelected = wardrobeViewModel::onCategorySelected,
                onFiltersChanged = wardrobeViewModel::onFilterChanged,
                onClearCategoryFilter = wardrobeViewModel::clearCategoryFilter,
                onClearColorFilter = wardrobeViewModel::clearColorFilter,
                onClearOccasionFilter = wardrobeViewModel::clearOccasionFilter,
                onClearSeasonFilter = wardrobeViewModel::clearSeasonFilter,
                onClearFilters = wardrobeViewModel::clearFilters,
                onAddClicked = { navController.navigate(AppRoute.Capture.route) },
                onBackClicked = { navController.popBackStack() },
                onItemClicked = { itemId -> navController.navigate(AppRoute.ItemDetail.create(itemId)) }
            )
        }
        composable(AppRoute.Capture.route) {
            val context = LocalContext.current
            val viewModel = remember {
                CaptureViewModel(
                    repository = appContainer.wardrobeRepository,
                    imageCaptureController = ImageCaptureController(
                        context = context.applicationContext,
                        contentResolver = context.contentResolver,
                        imageStorage = appContainer.imageStorage
                    )
                )
            }

            CaptureScreen(
                onBackClicked = { navController.popBackStack() },
                onSingleImported = { itemId ->
                    navController.navigate(AppRoute.ItemDetail.create(itemId))
                },
                onBatchImported = {
                    navController.navigate(AppRoute.ReviewQueue.route)
                },
                createCameraImageUri = viewModel::createCameraImageUri,
                importCameraPhoto = viewModel::importCameraPhoto,
                importSingle = viewModel::importSingle,
                importBatch = viewModel::importBatch
            )
        }
        composable(AppRoute.ReviewQueue.route) {
            val viewModel = remember {
                ReviewQueueViewModel(
                    repository = appContainer.wardrobeRepository,
                    imageStorage = appContainer.imageStorage
                )
            }
            val state by viewModel.uiState.collectAsState()

            ReviewQueueScreen(
                state = state,
                onBackClicked = { navController.popBackStack() },
                onCompleteDetails = { itemId ->
                    navController.navigate(AppRoute.ItemDetail.create(itemId))
                },
                onDeleteClicked = viewModel::delete
            )
        }
        composable(
            route = AppRoute.ItemDetail.route,
            arguments = listOf(navArgument("itemId") { type = NavType.LongType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getLong("itemId") ?: return@composable
            val viewModel = remember(itemId) {
                ItemDetailViewModel(
                    repository = appContainer.wardrobeRepository,
                    imageStorage = appContainer.imageStorage,
                    itemId = itemId
                )
            }
            val state by viewModel.formState.collectAsState()

            ItemDetailScreen(
                state = state,
                onNameChanged = viewModel::onNameChanged,
                onCategoryChanged = viewModel::onCategoryChanged,
                onColorChanged = viewModel::onColorChanged,
                onOccasionChanged = viewModel::onOccasionChanged,
                onFabricChanged = viewModel::onFabricChanged,
                onSeasonChanged = viewModel::onSeasonChanged,
                onNotesChanged = viewModel::onNotesChanged,
                onSaveClicked = {
                    viewModel.save {
                        navController.navigate(AppRoute.Wardrobe.route)
                    }
                },
                onDeleteClicked = {
                    viewModel.delete {
                        navController.navigate(AppRoute.Wardrobe.route)
                    }
                },
                onBackClicked = { navController.popBackStack() },
                showDelete = true
            )
        }
    }
}
