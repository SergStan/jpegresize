package com.stanserg.jpegresizeapp.data

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.OpenableColumns
import com.stanserg.jpegresizeapp.model.interfaces.LocalDataSource
import java.io.File
import java.io.FileOutputStream

class LocalDataSourceImpl(
    private val contentResolver: ContentResolver,
    private val cacheDirectory: File
) : LocalDataSource {

    override suspend fun compressPhoto(uri: Uri, quality: Int): File {
        val inputStream = contentResolver.openInputStream(uri) ?: throw NullPointerException()
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val outputFile = tempFileProvider()
        FileOutputStream(outputFile).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        }
        return outputFile
    }

    private fun tempFileProvider(): File {
        return File.createTempFile("compressed_", ".jpg", cacheDirectory)
    }

    override suspend fun loadImage(any: Any): Bitmap {
        return when (any) {
            is Uri -> {
                val inputStream = contentResolver.openInputStream(any)
                BitmapFactory.decodeStream(inputStream)
            }

            is File -> {
                val inputStream =  any.inputStream()
                BitmapFactory.decodeStream(inputStream)
            }
            else -> throw IllegalArgumentException("Unsupported source type")
        }
    }

    override suspend fun calculateFileSize(any: Any): Long {
        return try {
            when (any) {
                is Uri -> {
                    val cursor = contentResolver.query(any, null, null, null, null)
                    val sizeIndex = cursor?.getColumnIndex(OpenableColumns.SIZE) ?: -1
                    val size = if (cursor?.moveToFirst() == true && sizeIndex != -1) {
                        cursor.getLong(sizeIndex)
                    } else {
                        0L
                    }
                    cursor?.close()
                    size
                }

                is File -> any.length()
                else -> 0
            }
        } catch (e: Exception) {
            0
        }
    }
}

