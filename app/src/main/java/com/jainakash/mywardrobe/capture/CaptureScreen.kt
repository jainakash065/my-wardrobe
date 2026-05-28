package com.jainakash.mywardrobe.capture

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jainakash.mywardrobe.theme.WardrobeInk
import kotlinx.coroutines.launch

@Composable
fun CaptureScreen(
    onBackClicked: () -> Unit,
    onSingleImported: (Long) -> Unit,
    onBatchImported: () -> Unit,
    importSingle: suspend (Uri) -> Long,
    importBatch: suspend (List<Uri>) -> List<Long>
) {
    val coroutineScope = rememberCoroutineScope()
    var isImporting by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val singlePhotoPicker = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            coroutineScope.launch {
                isImporting = true
                errorMessage = null
                runCatching { importSingle(uri) }
                    .onSuccess(onSingleImported)
                    .onFailure { errorMessage = "Could not import image. Please try another photo." }
                isImporting = false
            }
        }
    }

    val batchPhotoPicker = rememberLauncherForActivityResult(PickMultipleVisualMedia()) { uris ->
        if (uris.isNotEmpty()) {
            coroutineScope.launch {
                isImporting = true
                errorMessage = null
                runCatching { importBatch(uris) }
                    .onSuccess { onBatchImported() }
                    .onFailure { errorMessage = "Could not import selected photos." }
                isImporting = false
            }
        }
    }

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
            text = "Capture",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
            color = WardrobeInk
        )
        Text(
            text = "Add one item now, or import a batch and review details later.",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            enabled = !isImporting,
            onClick = {
                singlePhotoPicker.launch(
                    PickVisualMediaRequest(PickVisualMedia.ImageOnly)
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add one item")
        }
        OutlinedButton(
            enabled = !isImporting,
            onClick = {
                batchPhotoPicker.launch(
                    PickVisualMediaRequest(PickVisualMedia.ImageOnly)
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Batch capture")
        }
        OutlinedButton(
            enabled = !isImporting,
            onClick = {
                singlePhotoPicker.launch(
                    PickVisualMediaRequest(PickVisualMedia.ImageOnly)
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Import from gallery")
        }
        if (isImporting) {
            Text("Importing...")
        }
        errorMessage?.let { message ->
            Text(message, color = MaterialTheme.colorScheme.error)
        }
    }
}
