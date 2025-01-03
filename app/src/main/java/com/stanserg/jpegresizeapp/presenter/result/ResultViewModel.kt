package com.stanserg.jpegresizeapp.presenter.result

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stanserg.jpegresizeapp.model.CalculateFileSizeUseCase
import com.stanserg.jpegresizeapp.model.LoadImageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class ResultViewModel(
    private val loadImageUseCase: LoadImageUseCase,
    private val calculateFileSizeUseCase: CalculateFileSizeUseCase
) : ViewModel() {
    private val _originalBitmap = MutableStateFlow<Bitmap?>(null)
    val originalBitmap: StateFlow<Bitmap?> get() = _originalBitmap

    private val _compressedBitmap = MutableStateFlow<Bitmap?>(null)
    val compressedBitmap: StateFlow<Bitmap?> get() = _compressedBitmap

    private val _fileSizes = MutableStateFlow<Pair<String, String>?>(null)
    val fileSizes: StateFlow<Pair<String, String>?> get() = _fileSizes

    fun loadImages(originalUri: Uri, compressedFile: File) {
        viewModelScope.launch {
            val originalBitmap = loadImageUseCase.execute(originalUri)
            val compressedBitmap = loadImageUseCase.execute(compressedFile)

            _originalBitmap.value = originalBitmap
            _compressedBitmap.value = compressedBitmap

            val originalSize = calculateFileSizeUseCase.execute(originalUri)
            val compressedSize = calculateFileSizeUseCase.execute(compressedFile)

            _fileSizes.value = Pair("Оригинал: $originalSize", "Сжатое: $compressedSize")
        }
    }
}