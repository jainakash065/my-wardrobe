package com.jainakash.mywardrobe.wardrobe

import androidx.compose.foundation.background
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
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
private fun FilterIcon(active: Boolean) {
    val color = if (active) WardrobeRose else WardrobeInk.copy(alpha = 0.72f)

    Canvas(modifier = Modifier.size(24.dp)) {
        val strokeWidth = 2.4.dp.toPx()
        val lineStart = size.width * 0.16f
        val lineEnd = size.width * 0.84f
        val yValues = listOf(size.height * 0.28f, size.height * 0.50f, size.height * 0.72f)
        val knobXValues = listOf(size.width * 0.62f, size.width * 0.38f, size.width * 0.70f)

        yValues.forEachIndexed { index, y ->
            drawLine(
                color = color,
                start = androidx.compose.ui.geometry.Offset(lineStart, y),
                end = androidx.compose.ui.geometry.Offset(lineEnd, y),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
            drawCircle(
                color = color,
                radius = 3.2.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(knobXValues[index], y)
            )
        }
    }
}

@Composable
fun WardrobeHomeScreen(
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
    onViewAllClicked: () -> Unit,
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Text(
                text = "Wardrobe",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                color = WardrobeInk
            )
            SearchFilterField(
                query = state.query,
                hasActiveFilters = state.hasActiveFilters,
                onQueryChanged = onQueryChanged,
                onFilterClicked = { showFilters = true }
            )
            DashboardStats(state = state)
            if (state.hasActiveFilters) {
                ActiveFilterRow(
                    filters = state.filters,
                    onClearCategoryFilter = onClearCategoryFilter,
                    onClearColorFilter = onClearColorFilter,
                    onClearOccasionFilter = onClearOccasionFilter,
                    onClearSeasonFilter = onClearSeasonFilter,
                    onClearFilters = onClearFilters
                )
            }
            DashboardSectionHeader(title = "Recently added", actionLabel = "View all", onActionClicked = onViewAllClicked)
            if (state.recentItems.isEmpty()) {
                EmptyWardrobeState(hasQueryOrFilter = false)
            } else {
                RecentItemRow(items = state.recentItems, onItemClicked = onItemClicked)
            }
            DashboardSectionHeader(title = "Browse by category", actionLabel = "View all", onActionClicked = onViewAllClicked)
            CategorySummaryGrid(
                summaries = state.categorySummaries,
                onCategoryClicked = { category ->
                    onCategorySelected(category)
                    onViewAllClicked()
                }
            )
            if (state.reviewItemCount > 0) {
                ReviewQueuePrompt(
                    count = state.reviewItemCount,
                    onReviewClicked = onReviewClicked
                )
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun WardrobeAllItemsScreen(
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
    onBackClicked: () -> Unit,
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = onBackClicked) {
                    Text("Back")
                }
                Text(
                    text = "All items",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = WardrobeInk
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            SearchFilterField(
                query = state.query,
                hasActiveFilters = state.hasActiveFilters,
                onQueryChanged = onQueryChanged,
                onFilterClicked = { showFilters = true }
            )
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
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "${state.items.size} ${if (state.items.size == 1) "item" else "items"}",
                style = MaterialTheme.typography.bodyMedium,
                color = WardrobeInk.copy(alpha = 0.72f)
            )
            Spacer(modifier = Modifier.height(12.dp))
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

@Composable
private fun SearchFilterField(
    query: String,
    hasActiveFilters: Boolean,
    onQueryChanged: (String) -> Unit,
    onFilterClicked: () -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        label = { Text("Search wardrobe") },
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = onFilterClicked) {
                FilterIcon(active = hasActiveFilters)
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun DashboardStats(state: WardrobeUiState) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        StatPill(
            icon = DashboardStatIcon.Items,
            label = "${state.totalItemCount}",
            caption = "items",
            modifier = Modifier.weight(1f)
        )
        StatPill(
            icon = DashboardStatIcon.Review,
            label = "${state.reviewItemCount}",
            caption = "need review",
            modifier = Modifier.weight(1f)
        )
        StatPill(
            icon = DashboardStatIcon.Recent,
            label = "${state.recentItems.size}",
            caption = "recent",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatPill(icon: DashboardStatIcon, label: String, caption: String, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = WardrobeTeal.copy(alpha = 0.10f)),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(WardrobeRose.copy(alpha = 0.14f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                DashboardStatIcon(icon = icon)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = label, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)
            Text(text = caption, style = MaterialTheme.typography.bodySmall, color = WardrobeInk.copy(alpha = 0.70f))
        }
    }
}

private enum class DashboardStatIcon {
    Items,
    Review,
    Recent
}

@Composable
private fun DashboardStatIcon(icon: DashboardStatIcon) {
    Canvas(modifier = Modifier.size(18.dp)) {
        val strokeWidth = 1.8.dp.toPx()
        val iconColor = WardrobeRose
        when (icon) {
            DashboardStatIcon.Items -> {
                val corner = 2.dp.toPx()
                drawRoundRect(
                    color = iconColor,
                    topLeft = androidx.compose.ui.geometry.Offset(size.width * 0.22f, size.height * 0.18f),
                    size = androidx.compose.ui.geometry.Size(size.width * 0.56f, size.height * 0.64f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(corner, corner),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth)
                )
                drawLine(
                    color = iconColor,
                    start = androidx.compose.ui.geometry.Offset(size.width * 0.34f, size.height * 0.34f),
                    end = androidx.compose.ui.geometry.Offset(size.width * 0.66f, size.height * 0.34f),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = iconColor,
                    start = androidx.compose.ui.geometry.Offset(size.width * 0.34f, size.height * 0.50f),
                    end = androidx.compose.ui.geometry.Offset(size.width * 0.66f, size.height * 0.50f),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
            }
            DashboardStatIcon.Review -> {
                drawCircle(
                    color = iconColor,
                    radius = size.minDimension * 0.34f,
                    center = androidx.compose.ui.geometry.Offset(size.width * 0.50f, size.height * 0.50f),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth)
                )
                drawLine(
                    color = iconColor,
                    start = androidx.compose.ui.geometry.Offset(size.width * 0.36f, size.height * 0.50f),
                    end = androidx.compose.ui.geometry.Offset(size.width * 0.47f, size.height * 0.62f),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = iconColor,
                    start = androidx.compose.ui.geometry.Offset(size.width * 0.47f, size.height * 0.62f),
                    end = androidx.compose.ui.geometry.Offset(size.width * 0.68f, size.height * 0.38f),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
            }
            DashboardStatIcon.Recent -> {
                drawCircle(
                    color = iconColor,
                    radius = size.minDimension * 0.35f,
                    center = androidx.compose.ui.geometry.Offset(size.width * 0.50f, size.height * 0.50f),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth)
                )
                drawLine(
                    color = iconColor,
                    start = androidx.compose.ui.geometry.Offset(size.width * 0.50f, size.height * 0.50f),
                    end = androidx.compose.ui.geometry.Offset(size.width * 0.50f, size.height * 0.30f),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = iconColor,
                    start = androidx.compose.ui.geometry.Offset(size.width * 0.50f, size.height * 0.50f),
                    end = androidx.compose.ui.geometry.Offset(size.width * 0.66f, size.height * 0.58f),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}

@Composable
private fun DashboardSectionHeader(title: String, actionLabel: String, onActionClicked: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = WardrobeInk,
            modifier = Modifier.weight(1f)
        )
        TextButton(onClick = onActionClicked) {
            Text("$actionLabel >")
        }
    }
}

@Composable
private fun RecentItemRow(items: List<WardrobeItem>, onItemClicked: (Long) -> Unit) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(items) { item ->
            RecentItemCard(item = item, onClick = { onItemClicked(item.id) })
        }
    }
}

@Composable
private fun RecentItemCard(item: WardrobeItem, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.width(132.dp)
    ) {
        AsyncImage(
            model = item.photoPath,
            contentDescription = item.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.84f)
                .background(Color.White)
        )
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = item.name.ifBlank { "Untitled item" }, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Text(text = item.category.displayName, style = MaterialTheme.typography.bodySmall, color = WardrobeInk.copy(alpha = 0.70f))
        }
    }
}

@Composable
private fun CategorySummaryGrid(
    summaries: List<CategorySummary>,
    onCategoryClicked: (WardrobeCategory) -> Unit
) {
    if (summaries.isEmpty()) {
        Text("Categories will appear after items are added.", color = WardrobeInk.copy(alpha = 0.70f))
        return
    }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        summaries.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                row.forEach { summary ->
                    CategorySummaryCard(
                        summary = summary,
                        onClick = { onCategoryClicked(summary.category) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (row.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun CategorySummaryCard(summary: CategorySummary, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(92.dp)
                    .background(WardrobeTeal.copy(alpha = 0.10f))
            ) {
                if (summary.previewPhotoPath.isNotBlank()) {
                    AsyncImage(
                        model = summary.previewPhotoPath,
                        contentDescription = summary.category.displayName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = summary.category.displayName,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${summary.count} ${if (summary.count == 1) "item" else "items"}",
                style = MaterialTheme.typography.bodySmall,
                color = WardrobeInk.copy(alpha = 0.70f)
            )
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
