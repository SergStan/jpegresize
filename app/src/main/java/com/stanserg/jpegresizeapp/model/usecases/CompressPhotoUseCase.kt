package com.stanserg.jpegresizeapp.model.usecases

import android.net.Uri
import com.stanserg.jpegresizeapp.model.interfaces.PhotoRepository
import java.io.File

class CompressPhotoUseCase(
    private val repository: PhotoRepository
) {
    suspend fun execute(uri: Uri, quality: Int): Result<File> {
        return try {
            val file = repository.compressPhoto(uri, quality)
            Result.success(file)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}