package com.github.dhaval2404.imagepicker.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.github.dhaval2404.imagepicker.R
import java.io.File

/**
 * Get Gallery/Camera Intent
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 04 January 2018
 */
object IntentUtils {

    /**
     * @return Intent Gallery Intent
     */
    fun getGalleryIntent(context: Context, mimeTypes: Array<String>): Intent {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val intent = getGalleryDocumentIntent(mimeTypes)
            if (intent.resolveActivity(context.packageManager) != null) {
                return intent
            }
        }
        return getLegacyGalleryPickIntent(mimeTypes)
    }

    /**
     * @return Intent Gallery Document Intent
     */
    private fun getGalleryDocumentIntent(mimeTypes: Array<String>): Intent {
        // Show Document Intent
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).applyImageTypes(mimeTypes)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        return intent
    }

    /**
     * @return Intent Gallery Pick Intent
     */
    private fun getLegacyGalleryPickIntent(mimeTypes: Array<String>): Intent {
        // Show Gallery Intent, Will open google photos
        val intent = Intent(Intent.ACTION_PICK).applyImageTypes(mimeTypes)
        return intent
    }

    private fun Intent.applyImageTypes(mimeTypes: Array<String>): Intent {
        // Apply filter to show image only in intent
        type = "image/*"
        if (mimeTypes.isNotEmpty()) {
            putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        }
        return this
    }

    /**
     * @return Intent Camera Intent
     */
    fun getCameraIntent(context: Context, file: File): Intent? {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // authority = com.github.dhaval2404.imagepicker.provider
            val authority =
                context.packageName + context.getString(R.string.image_picker_provider_authority_suffix)
            val photoURI = FileProvider.getUriForFile(context, authority, file)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file))
        }

        return intent
    }

    fun isCameraHardwareAvailable(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
    }
}
