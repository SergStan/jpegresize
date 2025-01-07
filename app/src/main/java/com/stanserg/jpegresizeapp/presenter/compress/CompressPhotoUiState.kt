package com.stanserg.jpegresizeapp.presenter.compress

import java.io.File

data class CompressPhotoUiState(
    val isLoading: Boolean = false,
    val previewFile: File? = null,
    val fileSizeInfo: Long = 0L,
    val errorMessage: String? = null
)