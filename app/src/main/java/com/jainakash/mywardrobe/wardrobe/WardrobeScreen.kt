package com.jainakash.mywardrobe.wardrobe

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jainakash.mywardrobe.domain.WardrobeCategory
import com.jainakash.mywardrobe.domain.WardrobeItem
import com.jainakash.mywardrobe.itemdetail.commonColors
import com.jainakash.mywardrobe.itemdetail.commonOccasions
import com.jainakash.mywardrobe.itemdetail.commonSeasons
import com.jainakash.mywardrobe.theme.WardrobeInk
import com.jainakash.mywardrobe.theme.WardrobeRose
import com.jainakash.mywardrobe.theme.WardrobeTeal

@Composable
fun WardrobeScreen(
    state: WardrobeUiState,
    onQueryChanged: (String) -> Unit,
    onCategorySelected: (WardrobeCategory?) -> Unit,
    onFiltersChanged: (WardrobeFilters) -> Unit,
    onClearCategoryFilter: () -> Unit,
    onClearColorFilter: () -> Unit,
    onClearOccasionFilter: () -> Unit,
    onClearSeasonFilter: () -> Unit,
    onClearFilters: () -> Unit,
    onAddClicked: () -> Unit,
    onReviewClicked: () -> Unit,
    onItemClicked: (Long) -> Unit
) {
    var showFilters by remember { mutableStateOf(false) }

    if (showFilters) {
        FilterSheet(
            filters = state.filters,
            onFiltersChanged = onFiltersChanged,
            onClearFilters = onClearFilters,
            onDismiss = { showFilters = false }
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClicked) {
                Text("+", style = MaterialTheme.typography.headlineSmall)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
        ) {
            Text(
                text = "Wardrobe",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                color = WardrobeInk
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = state.query,
                    onValueChange = onQueryChanged,
                    label = { Text("Search wardrobe") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                OutlinedButton(
                    onClick = { showFilters = true },
                    modifier = Modifier.height(56.dp)
                ) {
                    Text(if (state.hasActiveFilters) "Filters (${state.activeFilterLabels.size})" else "Filters")
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            CategoryRow(
                selectedCategory = state.selectedCategory,
                onCategorySelected = onCategorySelected
            )
            if (state.hasActiveFilters) {
                Spacer(modifier = Modifier.height(10.dp))
                ActiveFilterRow(
                    filters = state.filters,
                    onClearCategoryFilter = onClearCategoryFilter,
                    onClearColorFilter = onClearColorFilter,
                    onClearOccasionFilter = onClearOccasionFilter,
                    onClearSeasonFilter = onClearSeasonFilter,
                    onClearFilters = onClearFilters
                )
            }
            if (state.reviewItemCount > 0) {
                Spacer(modifier = Modifier.height(14.dp))
                ReviewQueuePrompt(
                    count = state.reviewItemCount,
                    onReviewClicked = onReviewClicked
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            if (state.items.isEmpty()) {
                EmptyWardrobeState(
                    hasQueryOrFilter = state.query.isNotBlank() || state.hasActiveFilters
                )
            } else {
                WardrobeGrid(items = state.items, onItemClicked = onItemClicked)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterSheet(
    filters: WardrobeFilters,
    onFiltersChanged: (WardrobeFilters) -> Unit,
    onClearFilters: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 28.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Filters",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = WardrobeInk,
                    modifier = Modifier.weight(1f)
                )
                TextButton(onClick = onClearFilters) {
                    Text("Clear all")
                }
            }
            FilterSection(title = "Category") {
                FilterOptionRow {
                    WardrobeCategory.entries.forEach { category ->
                        FilterChip(
                            selected = filters.category == category,
                            onClick = {
                                onFiltersChanged(
                                    filters.copy(
                                        category = category.takeUnless { filters.category == it }
                                    )
                                )
                            },
                            label = { Text(category.displayName) }
                        )
                    }
                }
            }
            FilterSection(title = "Color") {
                FilterOptionRow {
                    commonColors.forEach { color ->
                        FilterChip(
                            selected = filters.color.equals(color, ignoreCase = true),
                            onClick = {
                                onFiltersChanged(
                                    filters.copy(color = color.takeUnless { filters.color.equals(it, ignoreCase = true) } ?: "")
                                )
                            },
                            label = { Text(color) }
                        )
                    }
                }
            }
            FilterSection(title = "Occasion") {
                FilterOptionRow {
                    commonOccasions.forEach { occasion ->
                        FilterChip(
                            selected = filters.occasion.equals(occasion, ignoreCase = true),
                            onClick = {
                                onFiltersChanged(
                                    filters.copy(occasion = occasion.takeUnless { filters.occasion.equals(it, ignoreCase = true) } ?: "")
                                )
                            },
                            label = { Text(occasion) }
                        )
                    }
                }
            }
            FilterSection(title = "Season") {
                FilterOptionRow {
                    commonSeasons.forEach { season ->
                        FilterChip(
                            selected = filters.season.equals(season, ignoreCase = true),
                            onClick = {
                                onFiltersChanged(
                                    filters.copy(season = season.takeUnless { filters.season.equals(it, ignoreCase = true) } ?: "")
                                )
                            },
                            label = { Text(season) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterSection(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = WardrobeInk
        )
        content()
    }
}

@Composable
private fun FilterOptionRow(content: @Composable () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
    ) {
        content()
    }
}

@Composable
private fun ActiveFilterRow(
    filters: WardrobeFilters,
    onClearCategoryFilter: () -> Unit,
    onClearColorFilter: () -> Unit,
    onClearOccasionFilter: () -> Unit,
    onClearSeasonFilter: () -> Unit,
    onClearFilters: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
    ) {
        filters.category?.let { category ->
            FilterChip(
                selected = true,
                onClick = onClearCategoryFilter,
                label = { Text("Category: ${category.displayName}") }
            )
        }
        if (filters.color.isNotBlank()) {
            FilterChip(
                selected = true,
                onClick = onClearColorFilter,
                label = { Text("Color: ${filters.color}") }
            )
        }
        if (filters.occasion.isNotBlank()) {
            FilterChip(
                selected = true,
                onClick = onClearOccasionFilter,
                label = { Text("Occasion: ${filters.occasion}") }
            )
        }
        if (filters.season.isNotBlank()) {
            FilterChip(
                selected = true,
                onClick = onClearSeasonFilter,
                label = { Text("Season: ${filters.season}") }
            )
        }
        TextButton(onClick = onClearFilters) {
            Text("Clear")
        }
    }
}

@Composable
private fun ReviewQueuePrompt(count: Int, onReviewClicked: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = WardrobeRose.copy(alpha = 0.10f)),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "$count ${if (count == 1) "item needs" else "items need"} review",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = WardrobeInk
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Complete details or delete drafts.",
                    style = MaterialTheme.typography.bodySmall,
                    color = WardrobeInk.copy(alpha = 0.72f)
                )
            }
            OutlinedButton(onClick = onReviewClicked) {
                Text("Review")
            }
        }
    }
}

@Composable
private fun CategoryRow(
    selectedCategory: WardrobeCategory?,
    onCategorySelected: (WardrobeCategory?) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
    ) {
        FilterChip(
            selected = selectedCategory == null,
            onClick = { onCategorySelected(null) },
            label = { Text("All") }
        )
        WardrobeCategory.entries.forEach { category ->
            FilterChip(
                selected = selectedCategory == category,
                onClick = { onCategorySelected(category) },
                label = { Text(category.displayName) }
            )
        }
    }
}

@Composable
private fun EmptyWardrobeState(hasQueryOrFilter: Boolean) {
    Card(
        colors = CardDefaults.cardColors(containerColor = WardrobeTeal.copy(alpha = 0.10f)),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(WardrobeRose.copy(alpha = 0.18f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("+", style = MaterialTheme.typography.headlineSmall, color = WardrobeRose)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (hasQueryOrFilter) "No matching clothes" else "No clothes added yet",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (hasQueryOrFilter) {
                    "Try a different search or category."
                } else {
                    "Tap the add button when you are ready to start cataloging."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = WardrobeInk.copy(alpha = 0.72f)
            )
        }
    }
}

@Composable
private fun WardrobeGrid(items: List<WardrobeItem>, onItemClicked: (Long) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(bottom = 96.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items) { item ->
            WardrobeItemCard(item = item, onClick = { onItemClicked(item.id) })
        }
    }
}

@Composable
private fun WardrobeItemCard(item: WardrobeItem, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp)
    ) {
        AsyncImage(
            model = item.photoPath,
            contentDescription = item.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp)
                .background(Color.White)
        )
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "${item.color} - ${item.category.displayName}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
