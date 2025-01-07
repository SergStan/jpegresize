package com.stanserg.jpegresizeapp.model.interfaces

import android.graphics.Bitmap
import android.net.Uri
import java.io.File

interface LocalDataSource {

    suspend fun compressPhoto(uri: Uri, quality: Int): File
    suspend fun loadImage(file: File): File
    suspend fun calculateFileSize(uri: File): Long

}