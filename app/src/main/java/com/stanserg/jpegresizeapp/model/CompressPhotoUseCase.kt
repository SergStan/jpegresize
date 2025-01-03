package com.stanserg.jpegresizeapp.model

import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class CompressPhotoUseCase(private val compressor: (Uri, Int) -> File) {
    suspend fun execute(uri: Uri, quality: Int): File = withContext(Dispatchers.IO) {
        compressor(uri, quality)
    }
}