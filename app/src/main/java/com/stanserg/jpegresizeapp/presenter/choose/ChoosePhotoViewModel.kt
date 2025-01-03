package com.stanserg.jpegresizeapp.presenter.choose

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChoosePhotoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ChoosePhotoUiState())
    val uiState: StateFlow<ChoosePhotoUiState> get() = _uiState

    fun handlePhotoSelection(uri: Uri?) {
        _uiState.value = _uiState.value.copy(
            selectedPhotoUri = uri,
            isNextEnabled = uri != null
        )
    }
}