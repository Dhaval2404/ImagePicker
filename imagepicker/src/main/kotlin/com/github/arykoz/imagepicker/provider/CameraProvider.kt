package com.github.arykoz.imagepicker.provider

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityCompat.requestPermissions
import com.github.arykoz.imagepicker.ImagePicker
import com.github.arykoz.imagepicker.ImagePickerActivity
import com.github.arykoz.imagepicker.R
import com.github.arykoz.imagepicker.util.FileUtil
import com.github.arykoz.imagepicker.util.IntentUtils
import com.github.arykoz.imagepicker.util.PermissionUtil
import com.github.arykoz.imagepicker.util.PermissionUtil.isPermissionGranted
import java.io.File

/**
 * Capture new image using camera
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 04 January 2019
 */
class CameraProvider(activity: ImagePickerActivity, private val tryFrontCamera: Boolean = false) : BaseProvider(activity) {

    companion object {
        /**
         * Key to Save/Retrieve Camera File state
         */
        private const val STATE_CAMERA_FILE = "state.camera_file"

        /**
         * Permission Require for Image Capture using Camera
         */
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        /**
         * Permission Require for Image Capture using Camera
         */
        private val REQUIRED_PERMISSIONS_EXTENDED = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )

        private const val CAMERA_INTENT_REQ_CODE = 4281
        private const val PERMISSION_INTENT_REQ_CODE = 4282
    }

    /**
     * Temp Camera File
     */
    private var mCameraFile: File? = null

    /**
     * True If Camera Permission Defined in AndroidManifest.xml
     */
    private val mAskCameraPermission = PermissionUtil
        .isPermissionInManifest(this, Manifest.permission.CAMERA)

    /**
     * Camera image will be stored in below file directory
     */
    private var mFileDir: File? = null

    init {
        val bundle = activity.intent.extras!!

        // Get File Directory
        val fileDir = bundle.getString(ImagePicker.EXTRA_SAVE_DIRECTORY)
        fileDir?.let {
            mFileDir = File(it)
        }
    }

    /**
     * Save CameraProvider state

     * mCameraFile will lose its state when activity is recreated on
     * Orientation change or for Low memory device.
     *
     * Here, We Will save its state for later use
     *
     * Note: To produce this scenario, enable "Don't keep activities" from developer options
     **/
    override fun onSaveInstanceState(outState: Bundle) {
        // Save Camera File
        outState.putSerializable(STATE_CAMERA_FILE, mCameraFile)
    }

    /**
     * Retrieve CameraProvider state
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        // Restore Camera File
        mCameraFile = savedInstanceState?.getSerializable(STATE_CAMERA_FILE) as File?
    }

    /**
     * Start Camera Capture Intent
     */
    fun startIntent() {
        checkPermission()
    }

    /**
     * Check Require permission for Taking Picture.
     *
     * If permission is not granted request Permission, Else start Camera Intent
     */
    private fun checkPermission() {
        if (isPermissionGranted(this)) {
            // Permission Granted, Start Camera Intent
            startCameraIntent()
        } else {
            // Request Permission
            requestPermission()
        }
    }

    /**
     * Start Camera Intent
     *
     * Create Temporary File object and Pass it to Camera Intent
     */
    private fun startCameraIntent() {
        // Create and get empty file to store capture image content
        val file = FileUtil.getImageFile(dir = mFileDir)
        mCameraFile = file

        // Check if file exists
        if (file != null && file.exists()) {
            val cameraIntent = IntentUtils.getCameraIntent(this, file, tryFrontCamera)
            activity.startActivityForResult(cameraIntent, CAMERA_INTENT_REQ_CODE)
        } else {
            setError(R.string.error_failed_to_create_camera_image_file)
        }
    }

    /**
     * Handle Requested Permission Result
     */
    fun onRequestPermissionsResult(requestCode: Int) {
        if (requestCode == PERMISSION_INTENT_REQ_CODE) {
            // Check again if permission is granted
            if (isPermissionGranted(this)) {
                // Permission is granted, Start Camera Intent
                startCameraIntent()
            } else {
                // Exit with error message
                val errorRes = if (mAskCameraPermission) {
                    R.string.permission_camera_extended_denied
                } else {
                    R.string.permission_camera_denied
                }
                setError(getString(errorRes))
            }
        }
    }

    /**
     * Handle Camera Intent Activity Result
     *
     * @param requestCode It must be {@link CameraProvider#CAMERA_INTENT_REQ_CODE}
     * @param resultCode For success it should be {@link Activity#RESULT_OK}
     * @param data Result Intent
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAMERA_INTENT_REQ_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                handleResult(data)
            } else {
                setResultCancel()
            }
        }
    }

    /**
     * This method will be called when final result fot this provider is enabled.
     */
    private fun handleResult(data: Intent?) {
        activity.setImage(mCameraFile!!)
    }

    /**
     * Delete Camera file is exists
     */
    override fun onFailure() {
        mCameraFile?.delete()
    }

    /**
     * Request Runtime Permission required for Taking Pictures.
     *   Ref: https://github.com/Dhaval2404/ImagePicker/issues/34
     */
    private fun requestPermission() {
        if (mAskCameraPermission) {
            // If Camera permission defined in AndroidManifest then Need to request Camera Permission
            // Ref: https://github.com/Dhaval2404/ImagePicker/issues/34
            requestPermissions(activity, REQUIRED_PERMISSIONS_EXTENDED, PERMISSION_INTENT_REQ_CODE)
        } else {
            // If Camera permission is not defined in AndroidManifest then no need to request Camera Permission
            requestPermissions(activity, REQUIRED_PERMISSIONS, PERMISSION_INTENT_REQ_CODE)
        }
    }

    /**
     * Check if Check Require permission granted for Taking Picture.
     *   Ref: https://github.com/Dhaval2404/ImagePicker/issues/34
     *
     * @param context Application Context
     * @return boolean true if all required permission granted else false.
     */
    private fun isPermissionGranted(context: Context): Boolean {
        // Check if Camera permission defined in manifest
        if (mAskCameraPermission && isPermissionGranted(context, REQUIRED_PERMISSIONS_EXTENDED)) {
            // Camera and Storage permission is granted
            return true
        } else if (!mAskCameraPermission && isPermissionGranted(context, REQUIRED_PERMISSIONS)) {
            // Storage permission is granted
            return true
        }
        return false
    }
}
