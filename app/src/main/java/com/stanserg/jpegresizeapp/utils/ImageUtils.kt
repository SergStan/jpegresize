package com.stanserg.jpegresizeapp.utils


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

/**
 * Loads a Bitmap from the given InputStream.
 * @param inputStream The InputStream of the image.
 * @return A Bitmap object representing the loaded image.
 */
fun loadImage(inputStream: InputStream): Bitmap {
    return BitmapFactory.decodeStream(inputStream)
}

/**
 * Compresses an image from a given InputStream to a specified quality and saves it to a temporary file.
 * @param inputStream The InputStream of the image to compress.
 * @param quality The desired compression quality (1-100).
 * @param tempFileProvider A lambda to create a temporary file for saving the compressed image.
 * @return A File object containing the compressed image.
 */
fun compressImage(
    inputStream: InputStream,
    quality: Int,
    tempFileProvider: () -> File
): File {
    val bitmap = BitmapFactory.decodeStream(inputStream)
    val outputFile = tempFileProvider()

    FileOutputStream(outputFile).use { outputStream ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
    }
    return outputFile
}