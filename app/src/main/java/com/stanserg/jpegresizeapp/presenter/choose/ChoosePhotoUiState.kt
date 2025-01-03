package com.stanserg.jpegresizeapp.presenter.choose

import android.net.Uri

data class ChoosePhotoUiState(
    val selectedPhotoUri: Uri? = null,
    val isNextEnabled: Boolean = false
)