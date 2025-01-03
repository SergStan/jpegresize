package com.stanserg.jpegresizeapp.presenter.result

import android.graphics.Bitmap

data class ResultUiState(
    val originalBitmap: Bitmap? = null,
    val compressedBitmap: Bitmap? = null,
    val originalSizeText: String = "",
    val compressedSizeText: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)