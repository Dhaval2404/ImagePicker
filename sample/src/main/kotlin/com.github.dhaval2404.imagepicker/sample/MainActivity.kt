package com.github.dhaval2404.imagepicker.sample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_camera_only.*
import kotlinx.android.synthetic.main.content_gallery_only.*
import kotlinx.android.synthetic.main.content_profile.*

class MainActivity : AppCompatActivity() {

    companion object {

        private const val PROFILE_IMAGE_REQ_CODE = 101
        private const val GALLERY_IMAGE_REQ_CODE = 102
        private const val CAMERA_IMAGE_REQ_CODE = 103
        private const val DEFAULT_IMAGE_URL =
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTE2nnhjrSnA-nOn-pmBR1w1yIO5VytHaju-l-rUjNixn-w8oE4"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab_add_photo.setOnClickListener {
            ImagePicker.with(this)
                .cropSquare() // Crop Square image(Optional)
                .maxResultSize(620, 620) // Final image resolution will be less than 620 x 620(Optional)
                .start(PROFILE_IMAGE_REQ_CODE)
        }

        fab_add_gallery_photo.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly() // User can only select image from Gallery(Optional)
                .maxResultSize(1080, 1920) // Final image resolution will be less than 1080 x 1920(Optional)
                .start(GALLERY_IMAGE_REQ_CODE)
        }

        fab_add_camera_photo.setOnClickListener {
            ImagePicker.with(this)
                .provider(ImageProvider.CAMERA) // Default will be ImageProvider.BOTH
                .compress(1024) // Final image size will be less than 1 MB(Optional)
                .start(CAMERA_IMAGE_REQ_CODE)
        }

        imgProfile.setRemoteImage(DEFAULT_IMAGE_URL, true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            // File object will not be null for RESULT_OK
            val file = ImagePicker.getFile(data)

            Log.e("TAG", "Path:${file?.absolutePath}")
            when (requestCode) {
                PROFILE_IMAGE_REQ_CODE -> imgProfile.setLocalImage(file!!, true)
                GALLERY_IMAGE_REQ_CODE -> imgGallery.setLocalImage(file!!)
                CAMERA_IMAGE_REQ_CODE -> imgCamera.setLocalImage(file!!, false)
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}
