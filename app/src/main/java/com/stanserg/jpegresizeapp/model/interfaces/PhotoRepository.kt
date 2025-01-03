package com.stanserg.jpegresizeapp.model.interfaces

import android.graphics.Bitmap
import android.net.Uri
import java.io.File

interface PhotoRepository {
    suspend fun compressPhoto(uri: Uri, quality: Int): File
    suspend fun loadImage(any: Any): Bitmap
    suspend fun calculateFileSize(any: Any): Long
}