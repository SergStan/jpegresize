package com.stanserg.jpegresizeapp.presenter.compress

import android.graphics.Bitmap

data class CompressPhotoUiState(
    val isLoading: Boolean = false,
    val previewBitmap: Bitmap? = null,
    val fileSizeInfo: String = "",
    val errorMessage: String? = null
)