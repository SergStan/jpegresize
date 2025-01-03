package com.stanserg.jpegresizeapp.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stanserg.jpegresizeapp.model.CalculateFileSizeUseCase
import com.stanserg.jpegresizeapp.model.CompressPhotoUseCase
import com.stanserg.jpegresizeapp.model.LoadImageUseCase
import com.stanserg.jpegresizeapp.presenter.choose.ChoosePhotoViewModel
import com.stanserg.jpegresizeapp.presenter.compress.CompressPhotoViewModel
import com.stanserg.jpegresizeapp.presenter.result.ResultViewModel

class ViewModelFactory(
    private val compressPhotoUseCase: CompressPhotoUseCase,
    private val loadImageUseCase: LoadImageUseCase,
    private val calculateFileSizeUseCase: CalculateFileSizeUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CompressPhotoViewModel::class.java) -> {
                CompressPhotoViewModel(compressPhotoUseCase, loadImageUseCase) as T
            }
            modelClass.isAssignableFrom(ResultViewModel::class.java) -> {
                ResultViewModel(loadImageUseCase, calculateFileSizeUseCase) as T
            }
            modelClass.isAssignableFrom(ChoosePhotoViewModel::class.java) -> {
                ChoosePhotoViewModel() as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
        }
    }
}