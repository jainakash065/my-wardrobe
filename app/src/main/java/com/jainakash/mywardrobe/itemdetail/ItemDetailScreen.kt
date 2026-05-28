package com.jainakash.mywardrobe.itemdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jainakash.mywardrobe.domain.WardrobeCategory
import com.jainakash.mywardrobe.theme.WardrobeInk
import com.jainakash.mywardrobe.theme.WardrobeRose
import com.jainakash.mywardrobe.theme.WardrobeTeal

@Composable
fun ItemDetailScreen(
    state: ItemFormState,
    onNameChanged: (String) -> Unit,
    onCategoryChanged: (WardrobeCategory) -> Unit,
    onColorChanged: (String) -> Unit,
    onOccasionChanged: (String) -> Unit,
    onFabricChanged: (String) -> Unit,
    onSeasonChanged: (String) -> Unit,
    onNotesChanged: (String) -> Unit,
    onFavoriteChanged: (Boolean) -> Unit,
    onSaveClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onBackClicked: () -> Unit,
    showDelete: Boolean
) {
    Scaffold { padding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = onBackClicked) {
                    Text("Back")
                }
                Text(
                    text = "Item Detail",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = WardrobeInk
                )
            }
            PhotoPreview(photoPath = state.photoPath)
            FavoriteToggle(
                isFavorite = state.isFavorite,
                onFavoriteChanged = onFavoriteChanged
            )
            RequiredFields(
                state = state,
                onNameChanged = onNameChanged,
                onCategoryChanged = onCategoryChanged,
                onColorChanged = onColorChanged
            )
            OptionalFields(
                state = state,
                onOccasionChanged = onOccasionChanged,
                onFabricChanged = onFabricChanged,
                onSeasonChanged = onSeasonChanged,
                onNotesChanged = onNotesChanged
            )
            if (!state.isValid) {
                Text(
                    text = "Add a photo, name, and color to save this item.",
                    style = MaterialTheme.typography.bodySmall,
                    color = WardrobeInk.copy(alpha = 0.72f)
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = onSaveClicked,
                    enabled = state.isValid,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Save")
                }
                if (showDelete) {
                    OutlinedButton(onClick = onDeleteClicked, modifier = Modifier.weight(1f)) {
                        Text("Delete")
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoriteToggle(isFavorite: Boolean, onFavoriteChanged: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(WardrobeRose.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
            .padding(14.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Favorite",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = WardrobeInk
            )
            Text(
                text = if (isFavorite) "Marked for quick access" else "Add to favorites for quick access",
                style = MaterialTheme.typography.bodySmall,
                color = WardrobeInk.copy(alpha = 0.70f)
            )
        }
        Switch(checked = isFavorite, onCheckedChange = onFavoriteChanged)
    }
}

@Composable
private fun PhotoPreview(photoPath: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(WardrobeTeal.copy(alpha = 0.12f), RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (photoPath.isBlank()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(WardrobeRose.copy(alpha = 0.16f), RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text("Photo will appear here", color = WardrobeInk.copy(alpha = 0.70f))
            }
        } else {
            AsyncImage(
                model = photoPath,
                contentDescription = "Clothing photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun RequiredFields(
    state: ItemFormState,
    onNameChanged: (String) -> Unit,
    onCategoryChanged: (WardrobeCategory) -> Unit,
    onColorChanged: (String) -> Unit
) {
    SectionTitle("Basics")
    OutlinedTextField(
        value = state.name,
        onValueChange = onNameChanged,
        label = { Text("Name") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
    CategoryField(
        category = state.category,
        onCategoryChanged = onCategoryChanged
    )
    QuickPickTextField(
        label = "Color",
        value = state.color,
        options = commonColors,
        onValueChanged = onColorChanged,
        useColorSwatches = true
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryField(
    category: WardrobeCategory,
    onCategoryChanged: (WardrobeCategory) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = category.displayName,
            onValueChange = {},
            readOnly = true,
            label = { Text("Category") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            WardrobeCategory.entries.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.displayName) },
                    onClick = {
                        onCategoryChanged(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun OptionalFields(
    state: ItemFormState,
    onOccasionChanged: (String) -> Unit,
    onFabricChanged: (String) -> Unit,
    onSeasonChanged: (String) -> Unit,
    onNotesChanged: (String) -> Unit
) {
    SectionTitle("Details")
    QuickPickTextField(
        label = "Occasion",
        value = state.occasion,
        options = commonOccasions,
        onValueChanged = onOccasionChanged
    )
    OutlinedTextField(
        value = state.fabric,
        onValueChange = onFabricChanged,
        label = { Text("Fabric") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
    QuickPickTextField(
        label = "Season",
        value = state.season,
        options = commonSeasons,
        onValueChanged = onSeasonChanged
    )
    OutlinedTextField(
        value = state.notes,
        onValueChange = onNotesChanged,
        label = { Text("Notes") },
        minLines = 3,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = WardrobeInk
    )
}

@Composable
private fun QuickPickTextField(
    label: String,
    value: String,
    options: List<String>,
    onValueChanged: (String) -> Unit,
    useColorSwatches: Boolean = false
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChanged,
            label = { Text(label) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            options.forEach { option ->
                if (useColorSwatches) {
                    ColorOptionChip(
                        option = option,
                        selected = value.equals(option, ignoreCase = true),
                        onClick = { onValueChanged(option) }
                    )
                } else {
                    FilterChip(
                        selected = value.equals(option, ignoreCase = true),
                        onClick = { onValueChanged(option) },
                        label = { Text(option) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ColorOptionChip(
    option: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val style = styleForColorOption(option)
    val borderWidth = if (selected) 2.dp else 1.dp
    val borderColor = if (selected) WardrobeRose else style.borderColor
    val shape = RoundedCornerShape(18.dp)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(shape)
            .background(style.backgroundColor, shape)
            .border(borderWidth, borderColor, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .background(style.contentColor.copy(alpha = 0.18f), CircleShape)
                .border(1.dp, style.contentColor.copy(alpha = 0.50f), CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = option,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            color = style.contentColor
        )
    }
}
