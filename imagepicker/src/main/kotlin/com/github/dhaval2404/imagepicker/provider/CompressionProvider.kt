package com.github.dhaval2404.imagepicker.provider

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.ImagePickerActivity
import com.github.dhaval2404.imagepicker.util.FileUtil
import com.github.dhaval2404.imagepicker.util.ImageUtil
import java.io.File

/**
 * Compress Selected/Captured Image
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 04 January 2019
 */
class CompressionProvider(activity: ImagePickerActivity) : BaseProvider(activity) {

    companion object {
        private val TAG = CompressionProvider::class.java.simpleName
    }

    private val mMaxWidth: Int
    private val mMaxHeight: Int
    private val mMaxFileSize: Long

    private var mOriginalFile: File? = null
    private var mFileDir: File? = null

    init {
        val bundle = activity.intent.extras!!

        // Get Max Width/Height parameter from Intent
        mMaxWidth = bundle.getInt(ImagePicker.EXTRA_MAX_WIDTH, 0)
        mMaxHeight = bundle.getInt(ImagePicker.EXTRA_MAX_HEIGHT, 0)

        // Get Maximum Allowed file size
        mMaxFileSize = bundle.getLong(ImagePicker.EXTRA_IMAGE_MAX_SIZE, 0)

        // Get File Directory
        val fileDir = bundle.getString(ImagePicker.EXTRA_SAVE_DIRECTORY)
        fileDir?.let {
            mFileDir = File(it)
        }
    }

    /**
     * Check if compression should be enabled or not
     *
     * @return Boolean. True if Compression should be enabled else false.
     */
    fun isCompressEnabled(): Boolean {
        return mMaxFileSize > 0L
    }

    /**
     * Check if compression is required
     * @param file File object to apply Compression
     */
    fun isCompressionRequired(file: File): Boolean {
        val status = isCompressEnabled() && getSizeDiff(file) > 0L
        if (!status && mMaxWidth > 0 && mMaxHeight > 0) {
            // Check image resolution
            val sizes = getImageSize(file)
            return sizes[0] > mMaxWidth || sizes[1] > mMaxHeight
        }
        return status
    }

    private fun getSizeDiff(file: File): Long {
        return file.length() - mMaxFileSize
    }

    /**
     * Compress given file if enabled.
     *
     * @param file File to compress
     */
    fun compress(file: File) {
        startCompressionWorker(file)
    }

    /**
     * Start Compression in Background
     */
    @SuppressLint("StaticFieldLeak")
    private fun startCompressionWorker(file: File) {
        mOriginalFile = file
        object : AsyncTask<File, Void, File>() {
            override fun doInBackground(vararg params: File): File? {
                // Perform operation in background
                return startCompression(params[0])
            }

            override fun onPostExecute(file: File?) {
                super.onPostExecute(file)
                if (file != null) {
                    // Post Result
                    handleResult(file)
                } else {
                    // Post Error
                    setError(com.github.dhaval2404.imagepicker.R.string.error_failed_to_compress_image)
                }
            }
        }.execute(file)
    }

    /**
     * Check if compression required, And Apply compression until file size reach below Max Size.
     */
    private fun startCompression(file: File): File? {
        var newFile: File? = null
        var attempt = 0
        var lastAttempt = 0
        do {
            // Delete file if exist, fill will be exist in second loop.
            newFile?.delete()

            newFile = applyCompression(file, attempt)
            if (newFile == null) {
                return if (attempt > 0) {
                    applyCompression(file, lastAttempt)
                } else {
                    null
                }
            }
            lastAttempt = attempt

            if (mMaxFileSize > 0) {
                val diff = getSizeDiff(newFile)
                // Log.i(TAG, "Size Diff:$diff")
                attempt += when {
                    diff > 1024 * 1024 -> 3
                    diff > 500 * 1024 -> 2
                    else -> 1
                }
            } else {
                attempt++
            }
        } while (isCompressionRequired(newFile!!))

        return newFile
    }

    /**
     * Compress the file
     */
    private fun applyCompression(file: File, attempt: Int): File? {
        val resList = resolutionList()
        if (attempt >= resList.size) {
            return null
        }

        // Apply logic to get scaled bitmap resolution.
        val resolution = resList[attempt]
        var maxWidth = resolution[0]
        var maxHeight = resolution[1]

        if (mMaxWidth > 0 && mMaxHeight > 0) {
            if (maxWidth > mMaxWidth || maxHeight > mMaxHeight) {
                maxHeight = mMaxHeight
                maxWidth = mMaxWidth
            }
        }
        // Log.d(TAG, "maxWidth:$maxWidth, maxHeight:$maxHeight")

        // Check file format
        var format = Bitmap.CompressFormat.JPEG
        var quality = 90
        if (file.absolutePath.endsWith(".png")) {
            format = Bitmap.CompressFormat.PNG
            quality = 100
        }

        val compressFile: File? = FileUtil.getImageFile(dir = mFileDir)
        return if (compressFile != null) {
            ImageUtil.compressImage(
                file, maxWidth.toFloat(), maxHeight.toFloat(),
                format, quality, compressFile.absolutePath
            )
        } else {
            null
        }
    }

    /**
     * Image Resolution will be reduce with below parameters.
     *
     */
    private fun resolutionList(): List<IntArray> {
        return listOf(
            intArrayOf(2448, 3264), // 8.0 Megapixel
            intArrayOf(2008, 3032), // 6.0 Megapixel
            intArrayOf(1944, 2580), // 5.0 Megapixel
            intArrayOf(1680, 2240), // 4.0 Megapixel
            intArrayOf(1536, 2048), // 3.0 Megapixel
            intArrayOf(1200, 1600), // 2.0 Megapixel
            intArrayOf(1024, 1392), // 1.3 Megapixel
            intArrayOf(960, 1280), // 1.0 Megapixel
            intArrayOf(768, 1024), // 0.7 Megapixel
            intArrayOf(600, 800), // 0.4 Megapixel
            intArrayOf(480, 640), // 0.3 Megapixel
            intArrayOf(240, 320), // 0.15 Megapixel
            intArrayOf(120, 160), // 0.08 Megapixel
            intArrayOf(60, 80), // 0.04 Megapixel
            intArrayOf(30, 40) // 0.02 Megapixel
        )
    }

    /**
     * This method will be called when final result fot this provider is enabled.
     */
    private fun handleResult(file: File) {
        activity.setCompressedImage(file)
    }

    /**
     *
     * @param file File to get Image Size
     * @return Int Array, Index 0 has width and Index 1 has height
     */
    private fun getImageSize(file: File): IntArray {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(file.absolutePath, options)
        return intArrayOf(options.outWidth, options.outHeight)
    }
}
