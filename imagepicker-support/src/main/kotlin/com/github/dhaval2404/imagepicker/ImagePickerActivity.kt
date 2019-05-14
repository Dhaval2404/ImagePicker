package com.github.dhaval2404.imagepicker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import com.github.dhaval2404.imagepicker.provider.CameraProvider
import com.github.dhaval2404.imagepicker.provider.CompressionProvider
import com.github.dhaval2404.imagepicker.provider.CropProvider
import com.github.dhaval2404.imagepicker.provider.GalleryProvider
import java.io.File

/**
 * Pick Image
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 04 January 2019
 */
class ImagePickerActivity : FragmentActivity() {

    companion object {
        private const val TAG = "image_picker"

        internal fun getCancelledIntent(context: Context): Intent {
            val intent = Intent()
            val message = context.getString(R.string.error_task_cancelled)
            intent.putExtra(ImagePicker.EXTRA_ERROR, message)
            return intent
        }
    }

    private var mGalleryProvider: GalleryProvider? = null
    private var mCameraProvider: CameraProvider? = null
    private lateinit var mCropProvider: CropProvider
    private lateinit var mCompressionProvider: CompressionProvider

    private var mOriginalFile: File? = null
    private var mCropFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadBundle()
    }

    /**
     * Parse Intent Bundle and initialize variables
     */
    private fun loadBundle() {
        mCropProvider = CropProvider(this)
        mCompressionProvider = CompressionProvider(this)

        val bundle = intent.extras!!
        val provider = bundle.getSerializable(ImagePicker.EXTRA_IMAGE_PROVIDER) as ImageProvider

        // Create provider object and start process
        when (provider) {
            ImageProvider.GALLERY -> {
                mGalleryProvider = GalleryProvider(this)
                mGalleryProvider?.startIntent()
            }
            ImageProvider.CAMERA -> {
                mCameraProvider = CameraProvider(this)
                mCameraProvider?.startIntent()
            }
        }
    }

    /**
     * Dispatch incoming result to the correct provider.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mCameraProvider?.onRequestPermissionsResult(requestCode)
        mGalleryProvider?.onRequestPermissionsResult(requestCode)
    }

    /**
     * Dispatch incoming result to the correct provider.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mCameraProvider?.onActivityResult(requestCode, resultCode, data)
        mGalleryProvider?.onActivityResult(requestCode, resultCode, data)
        mCropProvider.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * Handle Activity Back Press
     */
    override fun onBackPressed() {
        setResultCancel()
    }

    /**
     * {@link CameraProvider} and {@link GalleryProvider} Result will be available here.
     *
     * @param file Capture/Gallery image file
     */
    fun setImage(file: File) {
        mOriginalFile = file
        when {
            mCropProvider.isCropEnabled() -> mCropProvider.startIntent(file)
            mCompressionProvider.isCompressionRequired(file) -> mCompressionProvider.compress(file)
            else -> setResult(file)
        }
    }

    /**
     * {@link CropProviders} Result will be available here.
     *
     * Check if compression is enable/required. If yes then start compression else return result.
     *
     * @param file Crop image file
     */
    fun setCropImage(file: File) {
        mCropFile = file

        mCameraProvider?.let {
            // Delete Camera file after crop. Else there will be two image for the same action.
            // In case of Gallery Provider, we will get original image path, so we will not delete that.
            mOriginalFile?.delete()
            mOriginalFile = null
        }

        if (mCompressionProvider.isCompressionRequired(file)) {
            mCompressionProvider.compress(file)
        } else {
            setResult(file)
        }
    }

    /**
     * {@link CompressionProvider} Result will be available here.
     *
     * @param file Compressed image file
     */
    fun setCompressedImage(file: File) {
        // This is the case when Crop is not enabled
        mCameraProvider?.let {
            // Delete Camera file after Compress. Else there will be two image for the same action.
            // In case of Gallery Provider, we will get original image path, so we will not delete that.
            mOriginalFile?.delete()
        }

        // If crop file is not null, Delete it after crop
        mCropFile?.delete()
        mCropFile = null

        setResult(file)
    }

    /**
     * Set Result, Image is successfully capture/picked/cropped/compressed.
     *
     * @param file final image file
     */
    private fun setResult(file: File) {
        val intent = Intent()
        intent.data = Uri.fromFile(file)
        intent.putExtra(ImagePicker.EXTRA_FILE_PATH, file.absolutePath)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    /**
     * User has cancelled the task
     */
    fun setResultCancel() {
        setResult(Activity.RESULT_CANCELED, getCancelledIntent(this))
        finish()
    }

    /**
     * Error occurred while processing image
     *
     * @param message Error Message
     */
    fun setError(message: String) {
        val intent = Intent()
        intent.putExtra(ImagePicker.EXTRA_ERROR, message)
        setResult(ImagePicker.RESULT_ERROR, intent)
        finish()
    }
}