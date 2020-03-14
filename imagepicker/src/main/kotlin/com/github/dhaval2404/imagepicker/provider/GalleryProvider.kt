package com.github.dhaval2404.imagepicker.provider

import android.Manifest
import android.app.Activity
import android.content.Intent
import androidx.core.app.ActivityCompat.requestPermissions
import com.github.dhaval2404.imagepicker.ImagePickerActivity
import com.github.dhaval2404.imagepicker.R
import com.github.dhaval2404.imagepicker.util.FileUriUtils
import com.github.dhaval2404.imagepicker.util.IntentUtils
import com.github.dhaval2404.imagepicker.util.PermissionUtil
import java.io.File

/**
 * Select image from Storage
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 04 January 2019
 */
class GalleryProvider(activity: ImagePickerActivity, private val mimeTypes: Array<String>) :
    BaseProvider(activity) {

    companion object {
        /**
         * Permission Require for Image Pick, For image pick just storage permission is need but
         * to crop or compress image write permission is also required. as both permission is in
         * same group, we have used write permission here.
         */
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        private const val GALLERY_INTENT_REQ_CODE = 4261
        private const val PERMISSION_INTENT_REQ_CODE = 4262
    }

    /**
     * Start Gallery Capture Intent
     */
    fun startIntent() {
        checkPermission()
    }

    /**
     * Check Require permission for Picking Gallery Image.
     *
     * If permission is not granted request Permission, Else start gallery Intent
     */
    private fun checkPermission() {
        if (!PermissionUtil.isPermissionGranted(this, REQUIRED_PERMISSIONS)) {
            requestPermissions(activity, REQUIRED_PERMISSIONS, PERMISSION_INTENT_REQ_CODE)
        } else {
            startGalleryIntent(mimeTypes)
        }
    }

    /**
     * Start Gallery Intent
     */
    private fun startGalleryIntent(mimeTypes: Array<String>) {
        val galleryIntent = IntentUtils.getGalleryIntent(activity, mimeTypes)
        activity.startActivityForResult(galleryIntent, GALLERY_INTENT_REQ_CODE)
    }

    /**
     * Handle Requested Permission Result
     */
    fun onRequestPermissionsResult(requestCode: Int) {
        if (requestCode == PERMISSION_INTENT_REQ_CODE) {
            // Check again if permission is granted
            if (PermissionUtil.isPermissionGranted(this, REQUIRED_PERMISSIONS)) {
                // Permission is granted, Start Camera Intent
                startGalleryIntent(mimeTypes)
            } else {
                // Exit with error message
                setError(getString(R.string.permission_gallery_denied))
            }
        }
    }

    /**
     * Handle Camera Intent Activity Result
     *
     * @param requestCode It must be {@link CameraProvider#GALLERY_INTENT_REQ_CODE}
     * @param resultCode For success it should be {@link Activity#RESULT_OK}
     * @param data Result Intent
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GALLERY_INTENT_REQ_CODE) {
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
        val uri = data?.data
        if (uri != null) {
            val filePath: String? = FileUriUtils.getRealPath(activity, uri)
            if (!filePath.isNullOrEmpty()) {
                activity.setImage(File(filePath))
            } else {
                setError(R.string.error_failed_pick_gallery_image)
            }
        } else {
            setError(R.string.error_failed_pick_gallery_image)
        }
    }
}
