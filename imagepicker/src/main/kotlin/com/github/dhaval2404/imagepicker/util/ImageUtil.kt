/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.dhaval2404.imagepicker.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.os.Build
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created on : June 18, 2016
 * Author     : zetbaitsu
 * Name       : Zetra
 * GitHub     : https://github.com/zetbaitsu
 *
 * For More Info Visit: https://github.com/zetbaitsu/Compressor
 */
object ImageUtil {

    @Throws(IOException::class)
    fun compressImage(
        imageFile: File,
        reqWidth: Float,
        reqHeight: Float,
        compressFormat: Bitmap.CompressFormat,
        destinationPath: String
    ): File {
        var fileOutputStream: FileOutputStream? = null
        val file = File(destinationPath).parentFile
        if (file?.exists() == false) {
            file.mkdirs()
        }
        try {
            fileOutputStream = FileOutputStream(destinationPath)
            // write the compressed bitmap at the destination specified by destinationPath.
            decodeSampledBitmapFromFile(imageFile, reqWidth, reqHeight)?.compress(
                compressFormat,
                100,
                fileOutputStream
            )
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.flush()
                fileOutputStream.close()
            }
        }

        return File(destinationPath)
    }

    @Throws(IOException::class)
    private fun decodeSampledBitmapFromFile(
        imageFile: File,
        reqWidth: Float,
        reqHeight: Float
    ): Bitmap? {
        // First decode with inJustDecodeBounds=true to check dimensions

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        var bmp: Bitmap? = BitmapFactory.decodeFile(imageFile.absolutePath, options)

        var actualHeight = options.outHeight
        var actualWidth = options.outWidth

        var imgRatio = actualWidth.toFloat() / actualHeight.toFloat()
        val maxRatio = reqWidth / reqHeight

        if (actualHeight > reqHeight || actualWidth > reqWidth) {
            // If Height is greater
            if (imgRatio < maxRatio) {
                imgRatio = reqHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = reqHeight.toInt()
            } // If Width is greater
            else if (imgRatio > maxRatio) {
                imgRatio = reqWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = reqWidth.toInt()
            } else {
                actualHeight = reqHeight.toInt()
                actualWidth = reqWidth.toInt()
            }
        }

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)
        options.inJustDecodeBounds = false

        if (bmp != null && canUseForInBitmap(bmp, options)) {
            // inBitmap only works with mutable bitmaps, so force the decoder to
            // return mutable bitmaps.
            options.inMutable = true
            options.inBitmap = bmp
        }
        options.inTempStorage = ByteArray(16 * 1024)

        try {
            bmp = BitmapFactory.decodeFile(imageFile.absolutePath, options)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }

        var scaledBitmap: Bitmap? = null
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }

        val ratioX = actualWidth / options.outWidth.toFloat()
        val ratioY = actualHeight / options.outHeight.toFloat()
        val middleX = actualWidth / 2.0f
        val middleY = actualHeight / 2.0f

        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)

        val canvas = Canvas(scaledBitmap!!)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(
            bmp!!, middleX - bmp.width / 2,
            middleY - bmp.height / 2, Paint(Paint.FILTER_BITMAP_FLAG)
        )
        bmp.recycle()

        val matrix = Matrix()
        scaledBitmap = Bitmap.createBitmap(
            scaledBitmap, 0, 0, scaledBitmap.width,
            scaledBitmap.height, matrix, true
        )

        return scaledBitmap
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    /**
     * Ref: https://developer.android.com/topic/performance/graphics/manage-memory#kotlin
     */
    private fun canUseForInBitmap(
        candidate: Bitmap,
        targetOptions: BitmapFactory.Options
    ): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // From Android 4.4 (KitKat) onward we can re-use if the byte size of
            // the new bitmap is smaller than the reusable bitmap candidate
            // allocation byte count.
            val width: Int = targetOptions.outWidth / targetOptions.inSampleSize
            val height: Int = targetOptions.outHeight / targetOptions.inSampleSize
            val byteCount: Int = width * height * getBytesPerPixel(candidate.config)
            byteCount <= candidate.allocationByteCount
        } else {
            // On earlier versions, the dimensions must match exactly and the inSampleSize must be 1
            candidate.width == targetOptions.outWidth &&
                candidate.height == targetOptions.outHeight &&
                targetOptions.inSampleSize == 1
        }
    }

    /**
     * A helper function to return the byte usage per pixel of a bitmap based on its configuration.
     */
    @Suppress("DEPRECATION")
    private fun getBytesPerPixel(config: Bitmap.Config): Int {
        return when (config) {
            Bitmap.Config.ARGB_8888 -> 4
            Bitmap.Config.RGB_565, Bitmap.Config.ARGB_4444 -> 2
            Bitmap.Config.ALPHA_8 -> 1
            else -> 1
        }
    }
}
