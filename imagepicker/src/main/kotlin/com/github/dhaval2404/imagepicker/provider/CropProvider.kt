package com.github.dhaval2404.imagepicker.provider

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.ImagePickerActivity
import com.github.dhaval2404.imagepicker.R
import com.github.dhaval2404.imagepicker.util.FileUriUtils
import com.github.dhaval2404.imagepicker.util.FileUtil
import com.yalantis.ucrop.UCrop
import java.io.File
import java.io.IOException

/**
 * Crop Selected/Captured Image
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 04 January 2019
 */
class CropProvider(activity: ImagePickerActivity, private val launcher: (Intent) -> Unit) :
    BaseProvider(activity) {

    companion object {
        private val TAG = CropProvider::class.java.simpleName
        private const val STATE_CROP_FILE = "state.crop_file"
    }

    private val maxWidth: Int
    private val maxHeight: Int

    private val crop: Boolean
    private val cropAspectX: Float
    private val cropAspectY: Float
    private var cropImageFile: File? = null

    init {
        with(activity.intent.extras ?: Bundle()) {
            maxWidth = getInt(ImagePicker.EXTRA_MAX_WIDTH, 0)
            maxHeight = getInt(ImagePicker.EXTRA_MAX_HEIGHT, 0)
            crop = getBoolean(ImagePicker.EXTRA_CROP, false)
            cropAspectX = getFloat(ImagePicker.EXTRA_CROP_X, 0f)
            cropAspectY = getFloat(ImagePicker.EXTRA_CROP_Y, 0f)
        }
    }

    /**
     * Save CameraProvider state
     *
     * mCropImageFile will lose its state when activity is recreated on
     * Orientation change or for Low memory device.
     *
     * Here, We Will save its state for later use
     *
     * Note: To produce this scenario, enable "Don't keep activities" from developer options
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(STATE_CROP_FILE, cropImageFile)
    }

    /**
     * Retrieve CropProvider state
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        cropImageFile = savedInstanceState?.getSerializable(STATE_CROP_FILE) as File?
    }

    /**
     * Check if crop should be enabled or not
     *
     * @return Boolean. True if Crop should be enabled else false.
     */
    fun isCropEnabled() = crop

    /**
     * Start Crop Activity
     */
    fun startIntent(file: File) {
        cropImage(file)
    }

    /**
     * @param file Image File to be cropped
     * @throws IOException if failed to crop image
     */
    @Throws(IOException::class)
    private fun cropImage(file: File) {
        val uri = Uri.fromFile(file)
        val extension = FileUriUtils.getImageExtension(uri)
        cropImageFile = FileUtil.getImageFile(extension = extension)

        if (cropImageFile == null || !cropImageFile!!.exists()) {
            Log.e(TAG, "Failed to create crop image file")
            setError(R.string.error_failed_to_crop_image)
            return
        }

        val options = UCrop.Options()
        options.setCompressionFormat(FileUtil.getCompressFormat(extension))
        val uCrop = UCrop.of(uri, Uri.fromFile(cropImageFile)).withOptions(options)

        if (cropAspectX > 0 && cropAspectY > 0) {
            uCrop.withAspectRatio(cropAspectX, cropAspectY)
        }

        if (maxWidth > 0 && maxHeight > 0) {
            uCrop.withMaxResultSize(maxWidth, maxHeight)
        }
        launcher.invoke(uCrop.getIntent(activity))
    }

    /**
     * This method will be called when final result fot this provider is enabled.
     */
    fun handleResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            val file = cropImageFile
            if (file != null) {
                activity.setCropImage(file)
            } else {
                setError(R.string.error_failed_to_crop_image)
            }
        } else {
            setResultCancel()
        }
    }

    /**
     * Delete Crop file is exists
     */
    override fun onFailure() {
        cropImageFile?.delete()
    }
}
