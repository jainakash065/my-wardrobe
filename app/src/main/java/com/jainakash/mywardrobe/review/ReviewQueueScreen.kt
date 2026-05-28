package com.jainakash.mywardrobe.review

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jainakash.mywardrobe.domain.WardrobeItem
import com.jainakash.mywardrobe.theme.WardrobeInk
import com.jainakash.mywardrobe.theme.WardrobeTeal

@Composable
fun ReviewQueueScreen(
    state: ReviewQueueUiState,
    onBackClicked: () -> Unit,
    onCompleteDetails: (Long) -> Unit,
    onDeleteClicked: (WardrobeItem) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        TextButton(onClick = onBackClicked) {
            Text("Back")
        }
        Text(
            text = "Review Queue",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
            color = WardrobeInk
        )
        Text(
            text = "Finish details for batch imports.",
            style = MaterialTheme.typography.bodyMedium
        )
        if (state.items.isEmpty()) {
            EmptyReviewQueue()
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.items) { item ->
                    ReviewQueueCard(
                        item = item,
                        onCompleteDetails = { onCompleteDetails(item.id) },
                        onDeleteClicked = { onDeleteClicked(item) }
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyReviewQueue() {
    Card(
        colors = CardDefaults.cardColors(containerColor = WardrobeTeal.copy(alpha = 0.10f)),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "No items need review.",
            modifier = Modifier.padding(24.dp),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun ReviewQueueCard(
    item: WardrobeItem,
    onCompleteDetails: () -> Unit,
    onDeleteClicked: () -> Unit
) {
    Card(shape = RoundedCornerShape(8.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(WardrobeTeal.copy(alpha = 0.10f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = item.photoPath,
                    contentDescription = "Item needing review",
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = missingFieldsText(item),
                style = MaterialTheme.typography.bodyMedium,
                color = WardrobeInk
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(onClick = onCompleteDetails, modifier = Modifier.weight(1f)) {
                    Text("Complete details")
                }
                OutlinedButton(onClick = onDeleteClicked, modifier = Modifier.weight(1f)) {
                    Text("Delete")
                }
            }
        }
    }
}

private fun missingFieldsText(item: WardrobeItem): String {
    val missing = buildList {
        if (item.name.isBlank()) add("name")
        if (item.color.isBlank()) add("color")
    }
    return "Missing: ${missing.joinToString()}"
}
