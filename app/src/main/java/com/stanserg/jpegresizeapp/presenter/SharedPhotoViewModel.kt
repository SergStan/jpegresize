package com.stanserg.jpegresizeapp.presenter

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File

class SharedPhotoViewModel: ViewModel() {
    val selectedPhotoIri = MutableLiveData<Uri>()
    val compressedPhotoFile = MutableLiveData<File>()
}