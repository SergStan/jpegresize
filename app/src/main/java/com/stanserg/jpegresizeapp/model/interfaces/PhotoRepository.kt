package com.stanserg.jpegresizeapp.model.interfaces

import android.net.Uri
import java.io.File

interface PhotoRepository {
    suspend fun compressPhoto(uri: Uri, quality: Int): File
    suspend fun loadImage(file: File): File
    suspend fun calculateFileSize(file: File): Long
}