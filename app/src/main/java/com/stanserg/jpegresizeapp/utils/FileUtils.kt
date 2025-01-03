package com.stanserg.jpegresizeapp.utils

import java.io.File

/**
 * Calculates the size of a file in bytes.
 * @param source The file whose size needs to be calculated.
 * @return The size of the file in bytes.
 */
fun calculateFileSize(source: File): Long {
    return source.length()
}