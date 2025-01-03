package com.stanserg.jpegresizeapp.di

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import com.stanserg.jpegresizeapp.model.CalculateFileSizeUseCase
import com.stanserg.jpegresizeapp.model.CompressPhotoUseCase
import com.stanserg.jpegresizeapp.model.LoadImageUseCase
import com.stanserg.jpegresizeapp.utils.calculateFileSize
import com.stanserg.jpegresizeapp.utils.compressImage
import com.stanserg.jpegresizeapp.utils.loadImage
import org.koin.dsl.module
import java.io.File

val useCaseModule = module {

    // Provide CompressPhotoUseCase
    single {
        CompressPhotoUseCase { uri, quality ->
            compressImage(
                inputStream = get<ContentResolver>().openInputStream(uri)!!,
                quality = quality,
                tempFileProvider = { File.createTempFile("compressed_", ".jpg", get<File>()) }
            )
        }
    }

    // Provide LoadImageUseCase
    single {
        LoadImageUseCase { source ->
            when (source) {
                is Uri -> loadImage(get<android.content.ContentResolver>().openInputStream(source)!!)
                is File -> loadImage(source.inputStream())
                else -> throw IllegalArgumentException("Unsupported source type")
            }
        }
    }


    // Provide CalculateFileSizeUseCase
    single {
        CalculateFileSizeUseCase { source ->
            calculateFileSize(
                source = source,
                fileSizeProvider = { any ->
                    when (any) {
                        is Uri -> {
                            val cursor = get<ContentResolver>().query(any, null, null, null, null)
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
                        else -> throw IllegalArgumentException("Unsupported source type")
                    }
                }
            )
        }
    }

    // Provide File (cache directory)
    single<File> { android.os.Environment.getExternalStorageDirectory() }

    // Provide ContentResolver
    single<ContentResolver> { android.content.ContextWrapper(android.app.Application()).contentResolver }
}