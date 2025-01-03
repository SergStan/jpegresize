package com.stanserg.jpegresizeapp.model.usecases

import com.stanserg.jpegresizeapp.model.interfaces.PhotoRepository

class CalculateFileSizeUseCase(
    private val repository: PhotoRepository
) {
    suspend fun execute(uri: Any): Result<String> {
        return try {
            val sizeInBytes = repository.calculateFileSize(uri)
            val sizeInKb = sizeInBytes / 1024
            val result = "$sizeInKb KB"
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}