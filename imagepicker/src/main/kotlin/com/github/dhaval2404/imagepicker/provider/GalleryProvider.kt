package com.github.dhaval2404.imagepicker.provider

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.core.app.ActivityCompat.requestPermissions
import com.github.dhaval2404.imagepicker.ImagePicker
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
class GalleryProvider(activity: ImagePickerActivity, private val launcher: (Intent) -> Unit) :
    BaseProvider(activity) {

    companion object {
        /**
         * Permission Require for Image Pick, For image pick just storage permission is need but
         * to crop or compress image write permission is also required. as both permission is in
         * same group, we have used write permission here.
         */
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        private const val PERMISSION_INTENT_REQ_CODE = 4262
    }

    // Mime types restrictions for gallery. By default all mime types are valid
    private val mimeTypes: Array<String>

    init {
        val bundle = activity.intent.extras ?: Bundle()
        mimeTypes = bundle.getStringArray(ImagePicker.EXTRA_MIME_TYPES) ?: emptyArray()
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
            startGalleryIntent()
        }
    }

    /**
     * Start Gallery Intent
     */
    private fun startGalleryIntent() {
        launcher.invoke(IntentUtils.getGalleryIntent(activity, mimeTypes))
    }

    /**
     * Handle Requested Permission Result
     */
    fun onRequestPermissionsResult(requestCode: Int) {
        if (requestCode == PERMISSION_INTENT_REQ_CODE) {
            // Check again if permission is granted
            if (PermissionUtil.isPermissionGranted(this, REQUIRED_PERMISSIONS)) {
                // Permission is granted, Start Camera Intent
                startGalleryIntent()
            } else {
                // Exit with error message
                setError(getString(R.string.permission_gallery_denied))
            }
        }
    }

    fun handleResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            handleResult(result.data)
        } else {
            setResultCancel()
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
