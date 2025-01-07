package com.stanserg.jpegresizeapp.presenter.choose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class ChoosePhotoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ChoosePhotoUiState())
    val uiState: StateFlow<ChoosePhotoUiState> get() = _uiState

    fun handlePhotoSelection(file: File?) {
        _uiState.value = _uiState.value.copy(
            selectedPhotoFile = file,
            isNextEnabled = file != null
        )
    }

    fun deleteFilesContainingWord(cacheDir: File?, keyword: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                cacheDir?.listFiles()?.forEach { file ->
                    if (file.name.contains(keyword, ignoreCase = true)) {
                        file.delete()
                    }
                }
            }
        }
    }
}