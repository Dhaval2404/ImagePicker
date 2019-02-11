package com.github.dhaval2404.imagepicker.util

import android.os.Environment
import android.os.StatFs
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * File Utility Methods
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 04 January 2019
 */
object FileUtil {

    /**
     * @return Return Empty file to store camera image.
     * @throws IOException if permission denied of failed to create new file.
     */
    fun getCameraFile(): File? {
        try {
            // Create an image file name
            val imageFileName = "IMG_${getTimestamp()}.jpg"

            //Create File Directory Object
            val storageDir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                "Camera"
            )

            //Create Directory If not exist
            if (!storageDir.exists()) storageDir.mkdirs()

            //Create File Object
            val file = File(storageDir, imageFileName)

            //Create empty file
            file.createNewFile()

            return file
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
    }

    /**
     * Get Current Time in yyyyMMdd HHmmssSSS format
     *
     * 2019/01/30 10:30:20 000
     * E.g. 20190130_103020000
     */
    private fun getTimestamp(): String {
        val timeFormat = "yyyyMMdd_HHmmssSSS"
        return SimpleDateFormat(timeFormat, Locale.getDefault()).format(Date())
    }

    /**
     * Get Free Space size
     * @param file directory object to check free space.
     */
    fun getFreeSpace(file: File): Long {
        val stat = StatFs(file.path)
        val availBlocks = stat.availableBlocksLong
        val blockSize = stat.blockSizeLong
        return availBlocks * blockSize
    }

}
