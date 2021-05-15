package com.github.dhaval2404.imagepicker.sample.util

import android.content.Context
import android.net.Uri
import android.util.Log
import com.github.dhaval2404.imagepicker.util.FileUriUtils
import com.github.dhaval2404.imagepicker.util.FileUtil
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * File Utility
 *
 * @author Dhaval Patel
 * @version 1.6
 * @since 05 January 2019
 */
object FileUtil {

    /**
     * @param context Context
     * @param uri Uri
     * @return Image Info
     */
    @JvmStatic
    fun getFileInfo(context: Context, uri: Uri?): String {
        if (uri == null) {
            return "Image not found"
        }

        // Get Resolution
        val resolution = FileUtil.getImageResolution(context, uri)

        // File Path
        val filePath = FileUriUtils.getRealPath(context, uri)
        val document = FileUtil.getDocumentFile(context, uri) ?: return "Image not found"

        // Get Last Modified
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.getDefault())
        val modified = sdf.format(document.lastModified())

        // File Size
        val fileSize = getFileSize(document.length())

        return StringBuilder()

            .append("Resolution: ")
            .append("${resolution.first}x${resolution.second}")
            .append("\n\n")

            .append("Modified: ")
            .append(modified)
            .append("\n\n")

            .append("File Size: ")
            .append(fileSize)
            .append("\n\n")

            /*.append("File Name: ")
            .append(getFileName(context.contentResolver, uri))
            .append("\n\n")*/

            .append("File Path: ")
            .append(filePath)
            .append("\n\n")

            .append("Uri Path: ")
            .append(uri.toString())
            .toString()
    }

    private fun getFileSize(fileSize: Long): String {
        val mb = fileSize / (1024 * 1024)
        val kb = fileSize / (1024)

        return if (mb > 1) {
            "$mb MB"
        } else {
            "$kb KB"
        }
    }

    /*private fun getFileName(contentResolver: ContentResolver, uri: Uri): String? {
        if (ContentResolver.SCHEME_FILE == uri.scheme) {
            return File(uri.path).getName()
        } else if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
            return getCursorContent(contentResolver, uri)
        }
        return null
    }

    private fun getCursorContent(
        contentResolver: ContentResolver,
        uri: Uri
    ): String? {
        return try {
            val cursor = contentResolver.query(uri, null, null, null, null) ?: return null
            var fileName: String? = null
            if (cursor.moveToFirst()) {
                fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
            cursor.close()
            fileName
        } catch (ex: Exception) {
            null
        }
    }*/

    fun printFileInfo(file: File?) {
        if (file == null) {
            Log.i("File Info", "File object is null")
            return
        }

        // Get Resolution
        val resolution = FileUtil.getImageResolution(file)

        val info = StringBuilder()
            .append("Resolution: ")
            .append("${resolution.first}x${resolution.second}")
            .append("\n")

            .append("File Size: ")
            .append(getFileSize(file.length()))
            .append("\n")

            .append("File Name: ")
            .append(file.name)
            .append("\n")

            .append("File Path: ")
            .append(file.absoluteFile)
            .toString()
        Log.i("File Info", info)
    }
}
