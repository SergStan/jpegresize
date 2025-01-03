package com.stanserg.jpegresizeapp.utils

/**
 * Calculates the size of a file in bytes.
 * @param source The file whose size needs to be calculated.
 * @return The size of the file in bytes.
 */
fun calculateFileSize(source: Any, fileSizeProvider: (Any) ->Long): Long {
    return fileSizeProvider(source)
}