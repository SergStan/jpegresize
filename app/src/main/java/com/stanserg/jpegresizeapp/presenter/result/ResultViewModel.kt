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

    fun loadImages(originalFile: File, compressedFile: File) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val originalFile =  withContext(Dispatchers.IO) {
                    loadImageUseCase.execute(originalFile).getOrThrow()
                }
                val compressedFile = withContext(Dispatchers.IO) {
                    loadImageUseCase.execute(compressedFile).getOrThrow()
                }

                val originalSize = withContext(Dispatchers.IO) {
                    calculateFileSizeUseCase.execute(originalFile).getOrThrow()
                }
                val compressedSize = withContext(Dispatchers.IO) {
                    calculateFileSizeUseCase.execute(compressedFile).getOrThrow()
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    originalBitmap = originalFile,
                    compressedBitmap = compressedFile,
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