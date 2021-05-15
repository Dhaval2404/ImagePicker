package com.github.dhaval2404.imagepicker.provider

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.ImagePickerActivity
import com.github.dhaval2404.imagepicker.R
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
class CropProvider(activity: ImagePickerActivity) : BaseProvider(activity) {

    companion object {
        private val TAG = CropProvider::class.java.simpleName

        /**
         * Key to Save/Retrieve Crop File state
         */
        private const val STATE_CROP_FILE = "state.crop_file"
    }

    private val mMaxWidth: Int
    private val mMaxHeight: Int

    private val mCrop: Boolean
    private val mCropAspectX: Float
    private val mCropAspectY: Float
    private var mCropImageFile: File? = null
    private val mFileDir: File

    init {
        val bundle = activity.intent.extras ?: Bundle()

        // Get Max Width/Height parameter from Intent
        mMaxWidth = bundle.getInt(ImagePicker.EXTRA_MAX_WIDTH, 0)
        mMaxHeight = bundle.getInt(ImagePicker.EXTRA_MAX_HEIGHT, 0)

        // Get Crop Aspect Ratio parameter from Intent
        mCrop = bundle.getBoolean(ImagePicker.EXTRA_CROP, false)
        mCropAspectX = bundle.getFloat(ImagePicker.EXTRA_CROP_X, 0f)
        mCropAspectY = bundle.getFloat(ImagePicker.EXTRA_CROP_Y, 0f)

        // Get File Directory
        val fileDir = bundle.getString(ImagePicker.EXTRA_SAVE_DIRECTORY)
        mFileDir = getFileDir(fileDir)
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
        // Save crop file
        outState.putSerializable(STATE_CROP_FILE, mCropImageFile)
    }

    /**
     * Retrieve CropProvider state
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        // Restore crop file
        mCropImageFile = savedInstanceState?.getSerializable(STATE_CROP_FILE) as File?
    }

    /**
     * Check if crop should be enabled or not
     *
     * @return Boolean. True if Crop should be enabled else false.
     */
    fun isCropEnabled() = mCrop

    /**
     * Start Crop Activity
     */
    fun startIntent(uri: Uri) {
        cropImage(uri)
    }

    /**
     * @param uri Uri to be cropped
     * @throws IOException if failed to crop image
     */
    @Throws(IOException::class)
    private fun cropImage(uri: Uri) {
        val extension = FileUtil.getImageExtension(uri)
        mCropImageFile = FileUtil.getImageFile(fileDir = mFileDir, extension = extension)

        if (mCropImageFile == null || !mCropImageFile!!.exists()) {
            Log.e(TAG, "Failed to create crop image file")
            setError(R.string.error_failed_to_crop_image)
            return
        }

        val options = UCrop.Options()
        options.setCompressionFormat(FileUtil.getCompressFormat(extension))

        val uCrop = UCrop.of(uri, Uri.fromFile(mCropImageFile))
            .withOptions(options)

        if (mCropAspectX > 0 && mCropAspectY > 0) {
            uCrop.withAspectRatio(mCropAspectX, mCropAspectY)
        }

        if (mMaxWidth > 0 && mMaxHeight > 0) {
            uCrop.withMaxResultSize(mMaxWidth, mMaxHeight)
        }

        try {
            uCrop.start(activity, UCrop.REQUEST_CROP)
        } catch (ex: ActivityNotFoundException) {
            setError(
                "uCrop not specified in manifest file." +
                    "Add UCropActivity in Manifest" +
                    "<activity\n" +
                    "    android:name=\"com.yalantis.ucrop.UCropActivity\"\n" +
                    "    android:screenOrientation=\"portrait\"\n" +
                    "    android:theme=\"@style/Theme.AppCompat.Light.NoActionBar\"/>"
            )
            ex.printStackTrace()
        }
    }

    /**
     * Handle Crop Intent Activity Result
     *
     * @param requestCode It must be {@link UCrop#REQUEST_CROP}
     * @param resultCode For success it should be {@link Activity#RESULT_OK}
     * @param data Result Intent
     */
    @Suppress("UNUSED_PARAMETER")
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == Activity.RESULT_OK) {
                handleResult(mCropImageFile)
            } else {
                setResultCancel()
            }
        }
    }

    /**
     * This method will be called when final result fot this provider is enabled.
     *
     * @param file cropped file
     */
    private fun handleResult(file: File?) {
        if (file != null) {
            activity.setCropImage(Uri.fromFile(file))
        } else {
            setError(R.string.error_failed_to_crop_image)
        }
    }

    /**
     * Handle Crop Failed
     */
    override fun onFailure() {
        delete()
    }

    /**
     * Delete Crop File, If not required
     *
     * After Image Compression, Crop File will not required
     */
    fun delete() {
        mCropImageFile?.delete()
        mCropImageFile = null
    }
}
