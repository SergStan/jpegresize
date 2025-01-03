package com.stanserg.jpegresizeapp.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class CalculateFileSizeUseCase(private val fileSizeCalculator: (Any) -> Long) {

    /**
     * Executes the file size calculation and returns a human-readable string.
     * @param source The source of the file (Uri or File).
     * @return The size of the file as a string in KB.
     */
    suspend fun execute(source: Any): String = withContext(Dispatchers.IO) {
        val sizeInBytes = fileSizeCalculator(source)
        val sizeInKb = sizeInBytes / 1024
        "$sizeInKb KB"
    }
}