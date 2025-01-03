package com.stanserg.jpegresizeapp.presenter.choose

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChoosePhotoViewModel : ViewModel() {

    private val _selectedPhotoUri = MutableStateFlow<Uri?>(null)
    val selectedPhotoUri: StateFlow<Uri?> get() = _selectedPhotoUri

    fun handlePhotoSelection(uri: Uri?) {
        _selectedPhotoUri.value = uri
    }

    fun pickPhoto(activity: Activity) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(intent, REQUEST_CODE_PICK_PHOTO)
    }

    companion object {
        const val REQUEST_CODE_PICK_PHOTO = 100
    }
}