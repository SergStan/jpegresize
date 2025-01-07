package com.stanserg.jpegresizeapp.data

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

    override suspend fun loadImage(file: File): File {
        return localDataSource.loadImage(file)
    }

    override suspend fun calculateFileSize(file: File): Long {
        return localDataSource.calculateFileSize(file)
    }
}