package com.github.dhaval2404.imagepicker.sample

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.sample.databinding.ActivityMainBinding
import com.github.dhaval2404.imagepicker.sample.extensions.runWithUri
import com.github.dhaval2404.imagepicker.sample.util.FileUtil
import com.github.dhaval2404.imagepicker.sample.util.IntentUtil
import com.github.dhaval2404.imagepicker.util.IntentUtils
import java.io.File

class MainActivity : AppCompatActivity() {

    companion object {
        private const val GITHUB_REPOSITORY = "https://github.com/Dhaval2404/ImagePicker"
    }

    private var mCameraUri: Uri? = null
    private var mGalleryUri: Uri? = null
    private var mProfileUri: Uri? = null

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.imgProfile.setDrawableImage(R.drawable.ic_person, true)

        // Setup listeners to pick image
        binding.fabAddPhoto.setOnClickListener(this::pickProfileImage)
        binding.fabAddGalleryPhoto.setOnClickListener(this::pickGalleryImage)
        binding.fabAddCameraPhoto.setOnClickListener(this::pickCameraImage)

        // Setup listeners to show image
        binding.imgProfile.setOnClickListener(this::showImage)
        binding.imgGallery.setOnClickListener(this::showImage)
        binding.imgCamera.setOnClickListener(this::showImage)

        // Setup listeners to show code
        binding.imgProfileCode.setOnClickListener(this::showImageCode)
        binding.imgGalleryCode.setOnClickListener(this::showImageCode)
        binding.imgCameraCode.setOnClickListener(this::showImageCode)

        // Setup listeners to show info
        binding.imgProfileInfo.setOnClickListener(this::showImageInfo)
        binding.imgGalleryInfo.setOnClickListener(this::showImageInfo)
        binding.imgCameraInfo.setOnClickListener(this::showImageInfo)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_github -> {
                IntentUtil.openURL(this, GITHUB_REPOSITORY)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            result.runWithUri(this) { uri ->
                mProfileUri = uri
                binding.imgProfile.setLocalImage(uri, true)
            }
        }

    private val startForGalleryImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            result.runWithUri(this) { uri ->
                mGalleryUri = uri
                binding.imgGallery.setLocalImage(uri)
            }
        }

    private val startForCameraImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            result.runWithUri(this) { uri ->
                mCameraUri = uri
                binding.imgCamera.setLocalImage(uri)
            }
        }

    @Suppress("UNUSED_PARAMETER")
    private fun pickProfileImage(view: View) {
        ImagePicker.with(this)
            // Crop Square image
            .cropSquare()
            .setImageProviderInterceptor { imageProvider -> // Intercept ImageProvider
                Log.d("ImagePicker", "Selected ImageProvider: " + imageProvider.name)
            }
            .setDismissListener {
                Log.d("ImagePicker", "Dialog Dismiss")
            }
            // Image resolution will be less than 512 x 512
            .maxResultSize(200, 200)
            .createIntent { intent -> startForProfileImageResult.launch(intent) }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun pickGalleryImage(view: View) {
        ImagePicker.with(this)
            // Crop Image(User can choose Aspect Ratio)
            .crop()
            // User can only select image from Gallery
            .galleryOnly()

            .galleryMimeTypes( // no gif images at all
                mimeTypes = arrayOf(
                    "image/png",
                    "image/jpg",
                    "image/jpeg"
                )
            )
            // Image resolution will be less than 1080 x 1920
            .maxResultSize(1080, 1920)
            // .saveDir(getExternalFilesDir(null)!!)
            .createIntent { intent -> startForGalleryImageResult.launch(intent) }
    }

    /**
     * Ref: https://gist.github.com/granoeste/5574148
     */
    @Suppress("UNUSED_PARAMETER")
    private fun pickCameraImage(view: View) {
        ImagePicker.with(this)
            // User can only capture image from Camera
            .cameraOnly()
            // Image size will be less than 1024 KB
            // .compress(1024)
            //  Path: /storage/sdcard0/Android/data/package/files
            .saveDir(getExternalFilesDir(null)!!)
            //  Path: /storage/sdcard0/Android/data/package/files/DCIM
            .saveDir(getExternalFilesDir(Environment.DIRECTORY_DCIM)!!)
            //  Path: /storage/sdcard0/Android/data/package/files/Download
            .saveDir(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!)
            //  Path: /storage/sdcard0/Android/data/package/files/Pictures
            .saveDir(getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!)
            //  Path: /storage/sdcard0/Android/data/package/files/Pictures/ImagePicker
            .saveDir(File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!, "ImagePicker"))
            //  Path: /storage/sdcard0/Android/data/package/files/ImagePicker
            .saveDir(getExternalFilesDir("ImagePicker")!!)
            //  Path: /storage/sdcard0/Android/data/package/cache/ImagePicker
            .saveDir(File(externalCacheDir, "ImagePicker"))
            //  Path: /data/data/package/cache/ImagePicker
            .saveDir(File(cacheDir, "ImagePicker"))
            //  Path: /data/data/package/files/ImagePicker
            .saveDir(File(filesDir, "ImagePicker"))

            // Below saveDir path will not work, So do not use it
            //  Path: /storage/sdcard0/DCIM
            //  .saveDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM))
            //  Path: /storage/sdcard0/Pictures
            //  .saveDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES))
            //  Path: /storage/sdcard0/ImagePicker
            //  .saveDir(File(Environment.getExternalStorageDirectory(), "ImagePicker"))

            .createIntent { intent -> startForCameraImageResult.launch(intent) }
    }

    private fun showImageCode(view: View) {
        val resource = when (view) {
            binding.imgProfileCode -> R.drawable.img_profile_code
            binding.imgCameraCode -> R.drawable.img_camera_code
            binding.imgGalleryCode -> R.drawable.img_gallery_code
            else -> 0
        }
        ImageViewerDialog.newInstance(resource).show(supportFragmentManager, "")
    }

    private fun showImage(view: View) {
        val uri = when (view) {
            binding.imgProfile -> mProfileUri
            binding.imgCamera -> mCameraUri
            binding.imgGallery -> mGalleryUri
            else -> null
        }

        uri?.let {
            startActivity(IntentUtils.getUriViewIntent(this, uri))
        }
    }

    private fun showImageInfo(view: View) {
        val uri = when (view) {
            binding.imgProfileInfo -> mProfileUri
            binding.imgCameraInfo -> mCameraUri
            binding.imgGalleryInfo -> mGalleryUri
            else -> null
        }

        AlertDialog.Builder(this)
            .setTitle("Image Info")
            .setMessage(FileUtil.getFileInfo(this, uri))
            .setPositiveButton("Ok", null)
            .show()
    }
}

val ActivityMainBinding.imgProfile get() = contentMain.contentProfile.imgProfile
val ActivityMainBinding.imgProfileCode get() = contentMain.contentProfile.imgProfileCode
val ActivityMainBinding.imgProfileInfo get() = contentMain.contentProfile.imgProfileInfo
val ActivityMainBinding.fabAddPhoto get() = contentMain.contentProfile.fabAddPhoto
val ActivityMainBinding.imgCamera get() = contentMain.contentCamera.imgCamera
val ActivityMainBinding.imgCameraCode get() = contentMain.contentCamera.imgCameraCode
val ActivityMainBinding.imgCameraInfo get() = contentMain.contentCamera.imgCameraInfo
val ActivityMainBinding.fabAddCameraPhoto get() = contentMain.contentCamera.fabAddCameraPhoto
val ActivityMainBinding.imgGallery get() = contentMain.contentGallery.imgGallery
val ActivityMainBinding.imgGalleryCode get() = contentMain.contentGallery.imgGalleryCode
val ActivityMainBinding.imgGalleryInfo get() = contentMain.contentGallery.imgGalleryInfo
val ActivityMainBinding.fabAddGalleryPhoto get() = contentMain.contentGallery.fabAddGalleryPhoto