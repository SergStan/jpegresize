package com.stanserg.jpegresizeapp.data

import android.graphics.Bitmap
import android.net.Uri
import com.stanserg.jpegresizeapp.model.interfaces.LocalDataSource
import com.stanserg.jpegresizeapp.model.interfaces.PhotoRepository
import java.io.File


class PhotoRepositoryImpl(
    private val localDataSource: LocalDataSource
) : PhotoRepository {
    override suspend fun compressPhoto(uri: Uri, quality: Int): File {
        return localDataSource.compressPhoto(uri, quality)
    }

    override suspend fun loadImage(any: Any): Bitmap {
        return localDataSource.loadImage(any)
    }

    override suspend fun calculateFileSize(any: Any): Long {
        return localDataSource.calculateFileSize(any)
    }
}