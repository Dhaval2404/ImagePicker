package com.github.dhaval2404.imagepicker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import com.github.dhaval2404.imagepicker.provider.CameraProvider
import com.github.dhaval2404.imagepicker.provider.CompressionProvider
import com.github.dhaval2404.imagepicker.provider.CropProvider
import com.github.dhaval2404.imagepicker.provider.GalleryProvider
import com.github.dhaval2404.imagepicker.util.FileUriUtils

/**
 * Pick Image
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 04 January 2019
 */
class ImagePickerActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadBundle(savedInstanceState)
    }

    /**
     * Save all appropriate activity state.
     */
    public override fun onSaveInstanceState(outState: Bundle) {
        mCameraProvider?.onSaveInstanceState(outState)
        mCropProvider.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    /**
     * Parse Intent Bundle and initialize variables
     */
    private fun loadBundle(savedInstanceState: Bundle?) {
        // Create Crop Provider
        mCropProvider = CropProvider(this)
        mCropProvider.onRestoreInstanceState(savedInstanceState)

        // Create Compression Provider
        mCompressionProvider = CompressionProvider(this)

        // Retrieve Image Provider

        // Create Gallery/Camera Provider
        when (intent?.getSerializableExtra(ImagePicker.EXTRA_IMAGE_PROVIDER)) {
            ImageProvider.GALLERY -> {
                mGalleryProvider = GalleryProvider(this)
                // Pick Gallery Image
                savedInstanceState ?: mGalleryProvider?.startIntent()
            }
            ImageProvider.CAMERA -> {
                mCameraProvider = CameraProvider(this)
                mCameraProvider?.onRestoreInstanceState(savedInstanceState)
                // Pick Camera Image
                savedInstanceState ?: mCameraProvider?.startIntent()
            }
            else -> {
                // Something went Wrong! This case should never happen
                Log.e(TAG, "Image provider can not be null")
                setError(getString(R.string.error_task_cancelled))
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
     * @param uri Capture/Gallery image Uri
     */
    fun setImage(uri: Uri) {
        when {
            mCropProvider.isCropEnabled() -> mCropProvider.startIntent(uri)
            mCompressionProvider.isCompressionRequired(uri) -> mCompressionProvider.compress(uri)
            else -> setResult(uri)
        }
    }

    /**
     * {@link CropProviders} Result will be available here.
     *
     * Check if compression is enable/required. If yes then start compression else return result.
     *
     * @param uri Crop image uri
     */
    fun setCropImage(uri: Uri) {
        // Delete Camera file after crop. Else there will be two image for the same action.
        // In case of Gallery Provider, we will get original image path, so we will not delete that.
        mCameraProvider?.delete()

        if (mCompressionProvider.isCompressionRequired(uri)) {
            mCompressionProvider.compress(uri)
        } else {
            setResult(uri)
        }
    }

    /**
     * {@link CompressionProvider} Result will be available here.
     *
     * @param uri Compressed image Uri
     */
    fun setCompressedImage(uri: Uri) {
        // This is the case when Crop is not enabled

        // Delete Camera file after crop. Else there will be two image for the same action.
        // In case of Gallery Provider, we will get original image path, so we will not delete that.
        mCameraProvider?.delete()

        // If crop file is not null, Delete it after crop
        mCropProvider.delete()

        setResult(uri)
    }

    /**
     * Set Result, Image is successfully capture/picked/cropped/compressed.
     *
     * @param uri final image Uri
     */
    private fun setResult(uri: Uri) {
        val intent = Intent()
        intent.data = uri
        intent.putExtra(ImagePicker.EXTRA_FILE_PATH, FileUriUtils.getRealPath(this, uri))
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

    /**
     * @param data Intent that store data
     * @param message Error message
     */
    fun setErrorResult(data: Intent?, message: String) {
        val intent = Intent()
        intent.data = data?.data
        intent.putExtra(ImagePicker.EXTRA_ERROR, message)
        setResult(ImagePicker.RESULT_ERROR, intent)
        finish()
    }
}
