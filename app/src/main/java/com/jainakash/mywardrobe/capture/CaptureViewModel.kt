package com.jainakash.mywardrobe.capture

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.jainakash.mywardrobe.data.WardrobeRepository
import com.jainakash.mywardrobe.domain.WardrobeItemDraft
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CaptureViewModel(
    private val repository: WardrobeRepository,
    private val imageCaptureController: ImageCaptureController,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    suspend fun importSingle(uri: Uri): Long = withContext(ioDispatcher) {
        val photoPath = imageCaptureController.saveGalleryImage(uri)
        repository.saveDraft(WardrobeItemDraft(photoPath = photoPath))
    }

    suspend fun importBatch(uris: List<Uri>): List<Long> =
        uris.map { uri -> importSingle(uri) }
}
