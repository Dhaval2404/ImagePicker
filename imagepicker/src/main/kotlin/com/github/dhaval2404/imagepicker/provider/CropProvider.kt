package com.github.dhaval2404.imagepicker.provider

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
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

    }

    private val mMaxWidth: Int
    private val mMaxHeight: Int

    private val mCropAspectX: Float
    private val mCropAspectY: Float
    private var mCropImageFile: File? = null

    init {
        val bundle = activity.intent.extras!!

        //Get Max Width/Height parameter from Intent
        mMaxWidth = bundle.getInt(ImagePicker.EXTRA_MAX_WIDTH, 0)
        mMaxHeight = bundle.getInt(ImagePicker.EXTRA_MAX_HEIGHT, 0)

        //Get Crop Aspect Ratio parameter from Intent
        mCropAspectX = bundle.getFloat(ImagePicker.EXTRA_CROP_X, 0f)
        mCropAspectY = bundle.getFloat(ImagePicker.EXTRA_CROP_Y, 0f)
    }

    /**
     * Check if crop should be enabled or not
     *
     * @return Boolean. True if Crop should be enabled else false.
     */
    fun isCropEnabled(): Boolean {
        return mCropAspectX > 0f && mCropAspectY > 0f
    }

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
        mCropImageFile = FileUtil.getCameraFile()

        if (mCropImageFile == null || !mCropImageFile!!.exists()) {
            Log.e(TAG, "Failed to create crop image file")
            setError(R.string.error_failed_to_crop_image)
            return
        }

        val options = UCrop.Options()
        val uCrop = UCrop.of(Uri.fromFile(file), Uri.fromFile(mCropImageFile))
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
     * @param requestCode  It must be {@link UCrop#REQUEST_CROP}
     * @param resultCode  For success it should be {@link Activity#RESULT_OK}
     * @param data Result Intent
     */
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
    private fun handleResult(file:File?) {
        if (file != null) {
            activity.setCropImage(file)
        } else {
            setError(R.string.error_failed_to_crop_image)
        }
    }

    /**
     * Delete Crop file is exists
     */
    override fun onFailure() {
        mCropImageFile?.delete()
    }

}