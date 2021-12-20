package com.github.dhaval2404.imagepicker.provider

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.ImagePickerActivity
import com.github.dhaval2404.imagepicker.R
import com.github.dhaval2404.imagepicker.util.IntentUtils

/**
 * Select image from Storage
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 04 January 2019
 */
class GalleryProvider(activity: ImagePickerActivity) :
    BaseProvider(activity) {

    companion object {
        private const val GALLERY_INTENT_REQ_CODE = 4261
    }

    // Mime types restrictions for gallery. By default all mime types are valid
    private val mimeTypes: Array<String>

    init {
        val bundle = activity.intent.extras ?: Bundle()

        // Get MIME types
        mimeTypes = bundle.getStringArray(ImagePicker.EXTRA_MIME_TYPES) ?: emptyArray()
    }

    /**
     * Start Gallery Capture Intent
     */
    fun startIntent() {
        startGalleryIntent()
    }

    /**
     * Start Gallery Intent
     */
    private fun startGalleryIntent() {
        val galleryIntent = IntentUtils.getGalleryIntent(activity, mimeTypes)
        activity.startActivityForResult(galleryIntent, GALLERY_INTENT_REQ_CODE)
    }

    /**
     * Handle Gallery Intent Activity Result
     *
     * @param requestCode It must be {@link GalleryProvider#GALLERY_INTENT_REQ_CODE}
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
            try {
                takePersistableUriPermission(uri)
                activity.setImage(uri)
            } catch (e: Exception) {
                activity.setErrorResult(data, e.message ?: "Unsupported file")
            }
        } else {
            setError(R.string.error_failed_pick_gallery_image)
        }
    }

    /**
     * Take a persistable URI permission grant that has been offered. Once
     * taken, the permission grant will be remembered across device reboots.
     */
    private fun takePersistableUriPermission(uri: Uri) {
        contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
}
