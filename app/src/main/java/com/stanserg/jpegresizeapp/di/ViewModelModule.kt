package com.stanserg.jpegresizeapp.di

import com.stanserg.jpegresizeapp.presenter.choose.ChoosePhotoViewModel
import com.stanserg.jpegresizeapp.presenter.compress.CompressPhotoViewModel
import com.stanserg.jpegresizeapp.presenter.result.ResultViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { ChoosePhotoViewModel() }

    viewModel {
        CompressPhotoViewModel(
            compressPhotoUseCase = get(),
            loadImageUseCase = get()
        )
    }

    viewModel {
        ResultViewModel(
            loadImageUseCase = get(),
            calculateFileSizeUseCase = get()
        )
    }
}

