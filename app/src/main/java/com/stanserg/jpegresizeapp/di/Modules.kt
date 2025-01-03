package com.stanserg.jpegresizeapp.di

import android.content.Context
import com.stanserg.jpegresizeapp.data.LocalDataSourceImpl
import com.stanserg.jpegresizeapp.data.PhotoRepositoryImpl
import com.stanserg.jpegresizeapp.model.usecases.CalculateFileSizeUseCase
import com.stanserg.jpegresizeapp.model.usecases.CompressPhotoUseCase
import com.stanserg.jpegresizeapp.model.usecases.LoadImageUseCase
import com.stanserg.jpegresizeapp.model.interfaces.LocalDataSource
import com.stanserg.jpegresizeapp.model.interfaces.PhotoRepository
import com.stanserg.jpegresizeapp.presenter.choose.ChoosePhotoViewModel
import com.stanserg.jpegresizeapp.presenter.compress.CompressPhotoViewModel
import com.stanserg.jpegresizeapp.presenter.result.ResultViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.io.File

val module = module {

        fun provideCacheDirectory(context: Context): File {
            val cacheDir = context.cacheDir
            if (!cacheDir.exists()) {
                cacheDir.mkdirs()
            }
            return cacheDir
        }

        single<File> { provideCacheDirectory(get()) }

        // Provide ContentResolver
        single { androidContext().contentResolver }
    }

// Data Layer
val repositoryModule = module {
        single<PhotoRepository> { PhotoRepositoryImpl(get()) }
        single<LocalDataSource> { LocalDataSourceImpl(get(), get()) }
    }

// Domain Layer
val useCaseModule = module {
    single { CompressPhotoUseCase(get()) }
    single { LoadImageUseCase(get()) }
    single { CalculateFileSizeUseCase(get()) }
}

// Presentation Layer
val viewModelModule = module {
    viewModel { ChoosePhotoViewModel() }
    viewModel { CompressPhotoViewModel(get(), get()) }
    viewModel { ResultViewModel(get(), get()) }

}