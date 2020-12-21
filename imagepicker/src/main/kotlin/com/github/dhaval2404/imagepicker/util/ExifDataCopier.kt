package com.github.dhaval2404.imagepicker.util

import android.util.Log
import androidx.exifinterface.media.ExifInterface
import java.io.File

/**
 * This file was taken from
 *     https://raw.githubusercontent.com/flutter/plugins/05879a3a4d8e582702227731ccdcf8b115f6b83d/packages/image_picker/image_picker/android/src/main/java/io/flutter/plugins/imagepicker/ExifDataCopier.java
 */
object ExifDataCopier {

    fun copyExif(filePathOri: File, filePathDest: File) {
        try {
            val oldExif = ExifInterface(filePathOri)
            val newExif = ExifInterface(filePathDest)
            val attributes: List<String> = listOf(
                "FNumber",
                "ExposureTime",
                "ISOSpeedRatings",
                "GPSAltitude",
                "GPSAltitudeRef",
                "FocalLength",
                "GPSDateStamp",
                "WhiteBalance",
                "GPSProcessingMethod",
                "GPSTimeStamp",
                "DateTime",
                "Flash",
                "GPSLatitude",
                "GPSLatitudeRef",
                "GPSLongitude",
                "GPSLongitudeRef",
                "Make",
                "Model",
                "Orientation"
            )
            for (attribute in attributes) {
                setIfNotNull(oldExif, newExif, attribute)
            }
            newExif.saveAttributes()
        } catch (ex: Exception) {
            Log.e("ExifDataCopier", "Error preserving Exif data on selected image: $ex")
        }
    }

    private fun setIfNotNull(oldExif: ExifInterface, newExif: ExifInterface, property: String) {
        if (oldExif.getAttribute(property) != null) {
            newExif.setAttribute(property, oldExif.getAttribute(property))
        }
    }
}
