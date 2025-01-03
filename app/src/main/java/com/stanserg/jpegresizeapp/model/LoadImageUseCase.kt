package com.stanserg.jpegresizeapp.model

import android.graphics.Bitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoadImageUseCase(private val loader: (Any) -> Bitmap) {
    suspend fun execute(source: Any): Bitmap = withContext(Dispatchers.IO) {
        loader(source)
    }
}