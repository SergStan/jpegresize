package com.stanserg.jpegresizeapp.presenter.choose

import java.io.File

data class ChoosePhotoUiState(
    val selectedPhotoFile: File? = null,
    val isNextEnabled: Boolean = false
)