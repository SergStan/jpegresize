package com.stanserg.jpegresizeapp

import android.app.Application
import com.stanserg.jpegresizeapp.di.useCaseModule
import com.stanserg.jpegresizeapp.di.viewModelModule
import org.koin.core.context.startKoin

class JpegApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(
                useCaseModule,
                viewModelModule
            )
        }
    }
}