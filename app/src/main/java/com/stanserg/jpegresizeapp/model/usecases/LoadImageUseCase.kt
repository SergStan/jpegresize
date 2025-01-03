package com.stanserg.jpegresizeapp.model.usecases

import android.graphics.Bitmap
import com.stanserg.jpegresizeapp.model.interfaces.PhotoRepository

class LoadImageUseCase(    private val repository: PhotoRepository
) {
    suspend fun execute(any: Any): Result<Bitmap> {
        return try {
            val file = repository.loadImage(any)
            Result.success(file)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}