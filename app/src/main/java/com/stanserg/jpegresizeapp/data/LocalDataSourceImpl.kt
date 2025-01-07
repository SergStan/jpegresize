package com.stanserg.jpegresizeapp.data

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.stanserg.jpegresizeapp.model.interfaces.LocalDataSource
import java.io.File
import java.io.FileOutputStream

class LocalDataSourceImpl(
    private val contentResolver: ContentResolver,
    private val cacheDirectory: File
) : LocalDataSource {

    override suspend fun compressPhoto(uri: Uri, quality: Int): File {
        return try {
            val bitmap = decodeBitmapWithSampleSize(uri, 1080, 1080)
                ?: throw IllegalStateException("Не удалось декодировать Bitmap из URI: $uri")

            val compressedFile = tempFileProvider()
            FileOutputStream(compressedFile).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            }
            bitmap.recycle()
            compressedFile
        } catch (e: Exception) {
            throw IllegalStateException("Ошибка при сжатии фото: ${e.message}", e)
        }
    }

    override suspend fun loadImage(file: File): File {
        return file
    }

    override suspend fun calculateFileSize(file: File): Long {
        return file.length()
    }


    private fun tempFileProvider(): File {
        return File.createTempFile("compressed_", ".jpg", cacheDirectory)
    }

    private fun decodeBitmapWithSampleSize(uri: Uri, reqWidth: Int, reqHeight: Int): Bitmap? {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true

        contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, options)
        }

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
        options.inJustDecodeBounds = false

        return contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, options)
        }
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}

