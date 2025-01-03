package com.stanserg.jpegresizeapp

import android.app.Application
import com.stanserg.jpegresizeapp.di.useCaseModule
import com.stanserg.jpegresizeapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class JpegApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@JpegApplication)
            modules(
                useCaseModule,
                viewModelModule
            )
        }
    }
}