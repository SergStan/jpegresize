package com.stanserg.jpegresizeapp.presenter.result
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stanserg.jpegresizeapp.model.usecases.CalculateFileSizeUseCase
import com.stanserg.jpegresizeapp.model.usecases.LoadImageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class ResultViewModel(
    private val loadImageUseCase: LoadImageUseCase,
    private val calculateFileSizeUseCase: CalculateFileSizeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ResultUiState())
    val uiState: StateFlow<ResultUiState> get() = _uiState

    fun loadImages(originalUri: Uri, compressedFile: File) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val originalBitmap =  withContext(Dispatchers.IO) {
                    loadImageUseCase.execute(originalUri).getOrThrow()
                }
                val compressedBitmap = withContext(Dispatchers.IO) {
                    loadImageUseCase.execute(compressedFile).getOrThrow()
                }

                val originalSize = withContext(Dispatchers.IO) {
                    calculateFileSizeUseCase.execute(originalUri).getOrThrow()
                }
                val compressedSize = withContext(Dispatchers.IO) {
                    calculateFileSizeUseCase.execute(compressedFile).getOrThrow()
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    originalBitmap = originalBitmap,
                    compressedBitmap = compressedBitmap,
                    originalSizeText = "Оригинал: $originalSize",
                    compressedSizeText = "Сжатое: $compressedSize"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Ошибка загрузки данных"
                )
            }
        }
    }
}