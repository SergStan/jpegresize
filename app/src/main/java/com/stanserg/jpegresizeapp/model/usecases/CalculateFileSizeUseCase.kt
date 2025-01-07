package com.stanserg.jpegresizeapp.model.usecases

import com.stanserg.jpegresizeapp.model.interfaces.PhotoRepository
import java.io.File

class CalculateFileSizeUseCase(
    private val repository: PhotoRepository
) {
    suspend fun execute(file: File): Result<String> {
        return try {
            val sizeInBytes = repository.calculateFileSize(file)
            val sizeInKb = sizeInBytes / 1024
            val result = "$sizeInKb KB"
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}