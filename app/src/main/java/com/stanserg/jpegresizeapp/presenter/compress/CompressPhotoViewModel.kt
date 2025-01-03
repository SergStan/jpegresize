package com.stanserg.jpegresizeapp.presenter.compress

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stanserg.jpegresizeapp.model.CompressPhotoUseCase
import com.stanserg.jpegresizeapp.model.LoadImageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class CompressPhotoViewModel(
    private val compressPhotoUseCase: CompressPhotoUseCase,
    private val loadImageUseCase: LoadImageUseCase
) : ViewModel() {
    private val _previewBitmap = MutableStateFlow<Bitmap?>(null)
    val previewBitmap: StateFlow<Bitmap?> get() = _previewBitmap

    private val _fileSizeInfo = MutableStateFlow("")
    val fileSizeInfo: StateFlow<String> get() = _fileSizeInfo

    private var originalUri: Uri? = null

    private var compressedFile: File? = null

    fun setOriginalUri(uri: Uri) {
        originalUri = uri
        updatePreview(50)
    }

    private fun updatePreview(compressionLevel: Int) {
        viewModelScope.launch {
            originalUri?.let { uri ->
                compressedFile = compressPhotoUseCase.execute(uri, compressionLevel)
                val bitmap = loadImageUseCase.execute(compressedFile!!)

                _previewBitmap.value = bitmap
                _fileSizeInfo.value = "Размер файла: ${compressedFile!!.length() / 1024} KB"
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