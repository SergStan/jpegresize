package com.stanserg.jpegresizeapp.presenter.result

import java.io.File

data class ResultUiState(
    val originalBitmap: File? = null,
    val compressedBitmap: File? = null,
    val originalSizeText: String = "",
    val compressedSizeText: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)