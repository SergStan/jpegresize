package com.stanserg.jpegresizeapp.model.usecases

import com.stanserg.jpegresizeapp.model.interfaces.PhotoRepository
import java.io.File

class LoadImageUseCase(private val repository: PhotoRepository
) {
    suspend fun execute(fileIn: File): Result<File> {
        return try {
            val file = repository.loadImage(fileIn)
            Result.success(file)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}