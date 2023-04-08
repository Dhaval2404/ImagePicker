package com.github.dhaval2404.imagepicker.provider

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.ImagePickerActivity
import com.github.dhaval2404.imagepicker.R

/**
 * Select image from Storage
 *
 * @author Dhaval Patel, Prathamesh More
 * @version 1.0
 * @since 04 January 2019
 */
class GalleryProvider(
    activity: ImagePickerActivity,
    private val galleryLauncher: ActivityResultLauncher<PickVisualMediaRequest>,
    private val galleryWithMimetypesLauncher: ActivityResultLauncher<Array<String>>
) :
    BaseProvider(activity) {

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
        // PickVisualMediaRequest supports only single mime type
        // so we will use it whenever 0 or 1 mimetype is specified and
        // use ACTION_OPEN_DOCUMENT otherwise
        if (mimeTypes.isEmpty()) {
            galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else if (mimeTypes.size == 1) {
            galleryLauncher.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.SingleMimeType(
                        mimeTypes[0]
                    )
                )
            )
        } else {
            galleryWithMimetypesLauncher.launch(mimeTypes)
        }
    }

    /**
     * Handle Gallery Intent Activity Result
     *
     * @param result URI of the selected image
     */
    fun onActivityResult(result: Uri?) {
        handleResult(result)
    }

    /**
     * This method will be called when final result fot this provider is enabled.
     */
    private fun handleResult(uri: Uri?) {
        if (uri != null) {
            takePersistableUriPermission(uri)
            activity.setImage(uri)
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
