package com.stanserg.jpegresizeapp.presenter.compress

import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stanserg.jpegresizeapp.model.usecases.CompressPhotoUseCase
import com.stanserg.jpegresizeapp.model.usecases.LoadImageUseCase
import com.stanserg.jpegresizeapp.utils.INIT_COMPRESSION_LEVEL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.io.File
import kotlinx.coroutines.withContext

class CompressPhotoViewModel(
    private val compressPhotoUseCase: CompressPhotoUseCase,
    private val loadImageUseCase: LoadImageUseCase
) : ViewModel() {

    private val compressionLevelFlow = MutableStateFlow(INIT_COMPRESSION_LEVEL)

    private val _uiState = MutableStateFlow(CompressPhotoUiState())
    val uiState: StateFlow<CompressPhotoUiState> get() = _uiState

    private var originalFile: File? = null
    private var compressedFile: File? = null

    init {
        viewModelScope.launch {
            compressionLevelFlow
                .debounce(100)
                .distinctUntilChanged()
                .collect { level ->
                    updatePreview(level)
                }
        }
    }

    fun setOriginalUri(file: File, compressionLevel: Int) {
        originalFile = file
        updatePreview(compressionLevel)
    }

    private fun updatePreview(compressionLevel: Int) {
        viewModelScope.launch {
            originalFile?.let { file ->
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                if (compressionLevel in 0..100) {
                    try {
                        val compressedFileResult = withContext(Dispatchers.IO) {
                            compressPhotoUseCase.execute(file.toUri(), compressionLevel)
                        }

                        compressedFileResult.onSuccess { comprFile ->
                            compressedFile = comprFile

                            val fileResult = withContext(Dispatchers.IO) {
                                loadImageUseCase.execute(comprFile)
                            }

                            fileResult.onSuccess { file ->
                                _uiState.value = _uiState.value.copy(
                                    isLoading = false,
                                    previewFile = file,
                                    fileSizeInfo = compressedFile!!.length()
                                )
                            }.onFailure {
                                throw it
                            }

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
                } else {
                    Log.w("TAAG", "level is not in 0.. 100 ")
                }
            }
        }
    }

    fun setCompressionLevel(level: Int) {
        compressionLevelFlow.value = level
    }

    fun getCompressedFile(): File? {
        return compressedFile
    }
}