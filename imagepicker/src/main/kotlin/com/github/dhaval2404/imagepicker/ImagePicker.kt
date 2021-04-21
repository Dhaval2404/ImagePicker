package com.github.dhaval2404.imagepicker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import com.github.dhaval2404.imagepicker.listener.DismissListener
import com.github.dhaval2404.imagepicker.listener.ResultListener
import com.github.dhaval2404.imagepicker.util.DialogHelper
import java.io.File

/**
 * Create Image Picker Object
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 04 January 2019
 */
@Suppress("unused")
open class ImagePicker {

    companion object {
        // Default Request Code to Pick Image
        const val RESULT_ERROR = 64

        internal const val EXTRA_IMAGE_PROVIDER = "extra.image_provider"
        internal const val EXTRA_CAMERA_DEVICE = "extra.camera_device"

        internal const val EXTRA_CROP = "extra.crop"
        internal const val EXTRA_CROP_X = "extra.crop_x"
        internal const val EXTRA_CROP_Y = "extra.crop_y"
        internal const val EXTRA_MAX_WIDTH = "extra.max_width"
        internal const val EXTRA_MAX_HEIGHT = "extra.max_height"
        internal const val EXTRA_KEEP_RATIO = "extra.keep_ratio"

        internal const val EXTRA_ERROR = "extra.error"
        internal const val EXTRA_FILE_PATH = "extra.file_path"
        internal const val EXTRA_MIME_TYPES = "extra.mime_types"

        /**
         * Use this to use ImagePicker in Activity Class
         *
         * @param activity Activity Instance
         */
        fun with(activity: Activity): Builder {
            return Builder(activity)
        }

        /**
         * Get error message from intent
         */
        fun getError(data: Intent?): String {
            val error = data?.getStringExtra(EXTRA_ERROR)
            if (error != null) {
                return error
            } else {
                return "Unknown Error!"
            }
        }

        /**
         * Get File Path from intent
         */
        fun getFilePath(data: Intent?): String? {
            return data?.getStringExtra(EXTRA_FILE_PATH)
        }

        /**
         * Get File from intent
         */
        fun getFile(data: Intent?): File? {
            val path = getFilePath(data)
            if (path != null) {
                return File(path)
            }
            return null
        }
    }

    class Builder(private val activity: Activity) {

        // Image Provider
        private var imageProvider = ImageProvider.BOTH

        // Mime types restrictions for gallery. by default all mime types are valid
        private var mimeTypes: Array<String> = emptyArray()

        /*
         * Crop Parameters
         */
        private var cropX: Float = 0f
        private var cropY: Float = 0f
        private var crop: Boolean = false

        /*
         * Resize Parameters
         */
        private var maxWidth: Int = 0
        private var maxHeight: Int = 0
        private var keepRatio = false

        private var imageProviderInterceptor: ((ImageProvider) -> Unit)? = null

        /**
         * Dialog dismiss event listener
         */
        private var dismissListener: DismissListener? = null

        /**
         * Specify Image Provider (Camera, Gallery or Both)
         */
        fun provider(imageProvider: ImageProvider): Builder {
            this.imageProvider = imageProvider
            return this
        }

        /**
         * Only Capture image using Camera.
         */
        // @Deprecated("Please use provider(ImageProvider.CAMERA) instead")
        fun cameraOnly(): Builder {
            this.imageProvider = ImageProvider.CAMERA
            return this
        }

        /**
         * Only Pick image from gallery.
         */
        // @Deprecated("Please use provider(ImageProvider.GALLERY) instead")
        fun galleryOnly(): Builder {
            this.imageProvider = ImageProvider.GALLERY
            return this
        }

        /**
         * Restrict mime types during gallery fetching, for instance if you do not want GIF images,
         * you can use arrayOf("image/png","image/jpeg","image/jpg")
         * by default array is empty, which indicates no additional restrictions, just images
         * @param mimeTypes
         */
        fun galleryMimeTypes(mimeTypes: Array<String>): Builder {
            this.mimeTypes = mimeTypes
            return this
        }

        /**
         * Set an aspect ratio for crop bounds.
         * User won't see the menu with other ratios options.
         *
         * @param x aspect ratio X
         * @param y aspect ratio Y
         */
        fun crop(x: Float, y: Float): Builder {
            cropX = x
            cropY = y
            return crop()
        }

        /**
         * Crop an image and let user set the aspect ratio.
         */
        fun crop(): Builder {
            this.crop = true
            return this
        }

        /**
         * Crop Square Image, Useful for Profile Image.
         *
         */
        fun cropSquare(): Builder {
            return crop(1f, 1f)
        }

        /**
         * Max Width and Height of final image
         */
        fun maxResultSize(width: Int, height: Int, keepRatio: Boolean = false): Builder {
            this.maxWidth = width
            this.maxHeight = height
            this.keepRatio = keepRatio
            return this
        }

        /**
         * Intercept Selected ImageProvider,  Useful for Analytics
         *
         * @param interceptor ImageProvider Interceptor
         */
        fun setImageProviderInterceptor(interceptor: (ImageProvider) -> Unit): Builder {
            this.imageProviderInterceptor = interceptor
            return this
        }

        /**
         * Sets the callback that will be called when the dialog is dismissed for any reason.
         */
        fun setDismissListener(listener: DismissListener): Builder {
            this.dismissListener = listener
            return this
        }

        /**
         * Sets the callback that will be called when the dialog is dismissed for any reason.
         */
        fun setDismissListener(listener: (() -> Unit)): Builder {
            this.dismissListener = object : DismissListener {
                override fun onDismiss() {
                    listener.invoke()
                }
            }
            return this
        }

        fun createIntent(): Intent =
            Intent(activity, ImagePickerActivity::class.java).apply { putExtras(getBundle()) }

        fun createIntentFromDialog(onResult: (Intent) -> Unit) {
            if (imageProvider == ImageProvider.BOTH) {
                DialogHelper.showChooseAppDialog(activity, object : ResultListener<ImageProvider> {
                    override fun onResult(t: ImageProvider?) {
                        t?.let {
                            imageProvider = it
                            imageProviderInterceptor?.invoke(imageProvider)
                            onResult(createIntent())
                        }
                    }
                }, dismissListener)
            }
        }

        /**
         * Get Bundle for ImagePickerActivity
         */
        private fun getBundle(): Bundle {
            return Bundle().apply {
                putSerializable(EXTRA_IMAGE_PROVIDER, imageProvider)
                putStringArray(EXTRA_MIME_TYPES, mimeTypes)
                putBoolean(EXTRA_CROP, crop)
                putFloat(EXTRA_CROP_X, cropX)
                putFloat(EXTRA_CROP_Y, cropY)
                putInt(EXTRA_MAX_WIDTH, maxWidth)
                putInt(EXTRA_MAX_HEIGHT, maxHeight)
                putBoolean(EXTRA_KEEP_RATIO, keepRatio)
            }
        }
    }
}