package com.stanserg.jpegresizeapp.presenter.compress

import android.graphics.Bitmap
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stanserg.jpegresizeapp.model.usecases.CompressPhotoUseCase
import com.stanserg.jpegresizeapp.model.usecases.LoadImageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import kotlinx.coroutines.withContext

class CompressPhotoViewModel(
    private val compressPhotoUseCase: CompressPhotoUseCase,
    private val loadImageUseCase: LoadImageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CompressPhotoUiState())
    val uiState: StateFlow<CompressPhotoUiState> get() = _uiState

    private var originalUri: Uri? = null
    private var compressedFile: File? = null

    fun setOriginalUri(uri: Uri) {
        originalUri = uri
        updatePreview(50)
    }

    private fun updatePreview(compressionLevel: Int) {
        viewModelScope.launch {
            originalUri?.let { uri ->

                var bitmapPr: Bitmap? = null
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

                try {
                    val compressedFileResult = withContext(Dispatchers.IO) {
                        compressPhotoUseCase.execute(uri, compressionLevel)
                    }

                    compressedFileResult.onSuccess { file ->
                        compressedFile = file

                        val bitmap = withContext(Dispatchers.IO) {
                            loadImageUseCase.execute(file.toUri())
                        }

                       withContext(Dispatchers.IO){ bitmap.onSuccess {
                          bitmapPr = it
                        }}

                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            previewBitmap = bitmapPr,
                            fileSizeInfo = "Размер файла: ${file.length() / 1024} KB"
                        )
                    }.onFailure { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = exception.message ?: "Ошибка при сжатии фото"
                        )
                    }
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Неизвестная ошибка"
                    )
                }
            }
        }
    }
    fun setCompressionLevel(level: Int) {
        updatePreview(level)
    }

    fun getCompressedFile(): File? {
        return compressedFile
    }
}