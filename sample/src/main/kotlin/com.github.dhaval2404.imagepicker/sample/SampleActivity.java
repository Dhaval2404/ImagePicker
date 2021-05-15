package com.github.dhaval2404.imagepicker.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.github.dhaval2404.imagepicker.constant.ImageProvider;
import com.github.dhaval2404.imagepicker.listener.DismissListener;
import com.github.dhaval2404.imagepicker.sample.util.FileUtil;
import com.github.dhaval2404.imagepicker.sample.util.IntentUtil;
import com.github.dhaval2404.imagepicker.util.IntentUtils;

import java.io.File;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class SampleActivity extends AppCompatActivity {

    private static final String GITHUB_REPOSITORY = "https://github.com/Dhaval2404/ImagePicker";

    private static final int PROFILE_IMAGE_REQ_CODE = 101;
    private static final int GALLERY_IMAGE_REQ_CODE = 102;
    private static final int CAMERA_IMAGE_REQ_CODE = 103;

    private Uri mCameraUri;
    private Uri mGalleryUri;
    private Uri mProfileUri;

    private ImageView imgProfileInfo;
    private ImageView imgCameraInfo;
    private ImageView imgGalleryInfo;

    private ImageView imgProfile;
    private ImageView imgGallery;
    private ImageView imgCamera;

    private ImageView imgProfileCode;
    private ImageView imgGalleryCode;
    private ImageView imgCameraCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imgProfileInfo = findViewById(R.id.imgProfileInfo);
        imgCameraInfo = findViewById(R.id.imgCameraInfo);
        imgGalleryInfo = findViewById(R.id.imgGalleryInfo);

        imgProfile = findViewById(R.id.imgProfile);
        imgCamera = findViewById(R.id.imgCamera);
        imgGallery = findViewById(R.id.imgGallery);

        imgProfileCode = findViewById(R.id.imgProfileCode);
        imgCameraCode = findViewById(R.id.imgCameraCode);
        imgGalleryCode = findViewById(R.id.imgGalleryCode);

        ImageViewExtensionKt.setDrawableImage(imgProfile, R.drawable.ic_person, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_github) {
            IntentUtil.openURL(this, GITHUB_REPOSITORY);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void pickProfileImage(View view) {
        ImagePicker.with(this)
                // Crop Square image
                .cropSquare()
                .setImageProviderInterceptor(new Function1<ImageProvider, Unit>() {
                    @Override
                    public Unit invoke(ImageProvider imageProvider) {
                        Log.d("ImagePicker", "Selected ImageProvider: " + imageProvider.toString());
                        return null;
                    }
                }).setDismissListener(new DismissListener() {
            @Override
            public void onDismiss() {
                Log.d("ImagePicker", "Dialog Dismiss");
            }
        })
                // Image resolution will be less than 512 x 512
                .maxResultSize(200, 200)
                .start(PROFILE_IMAGE_REQ_CODE);
    }

    public void pickGalleryImage(View view) {
        ImagePicker.with(this)
                // Crop Image(User can choose Aspect Ratio)
                .crop()
                // User can only select image from Gallery
                .galleryOnly()

                .galleryMimeTypes(new String[]{"image/png",
                        "image/jpg",
                        "image/jpeg"
                })
                // Image resolution will be less than 1080 x 1920
                .maxResultSize(1080, 1920)
                // .saveDir(getExternalFilesDir(null))
                .start(GALLERY_IMAGE_REQ_CODE);
    }

    /**
     * Ref: https://gist.github.com/granoeste/5574148
     */
    public void pickCameraImage(View view) {
        ImagePicker.with(this)
                // User can only capture image from Camera
                .cameraOnly()
                // Image size will be less than 1024 KB
                // .compress(1024)
                //  Path: /storage/sdcard0/Android/data/package/files
                .saveDir(getExternalFilesDir(null))
                //  Path: /storage/sdcard0/Android/data/package/files/DCIM
                .saveDir(getExternalFilesDir(Environment.DIRECTORY_DCIM))
                //  Path: /storage/sdcard0/Android/data/package/files/Download
                .saveDir(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS))
                //  Path: /storage/sdcard0/Android/data/package/files/Pictures
                .saveDir(getExternalFilesDir(Environment.DIRECTORY_PICTURES))
                //  Path: /storage/sdcard0/Android/data/package/files/Pictures/ImagePicker
                .saveDir(new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ImagePicker"))
                //  Path: /storage/sdcard0/Android/data/package/files/ImagePicker
                .saveDir(getExternalFilesDir("ImagePicker"))
                //  Path: /storage/sdcard0/Android/data/package/cache/ImagePicker
                .saveDir(new File(getExternalCacheDir(), "ImagePicker"))
                //  Path: /data/data/package/cache/ImagePicker
                .saveDir(new File(getCacheDir(), "ImagePicker"))
                //  Path: /data/data/package/files/ImagePicker
                .saveDir(new File(getFilesDir(), "ImagePicker"))

                // Below saveDir path will not work, So do not use it
                //  Path: /storage/sdcard0/DCIM
                //  .saveDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM))
                //  Path: /storage/sdcard0/Pictures
                //  .saveDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES))
                //  Path: /storage/sdcard0/ImagePicker
                //  .saveDir(File(Environment.getExternalStorageDirectory(), "ImagePicker"))

                .start(CAMERA_IMAGE_REQ_CODE);
    }

    public void showImageCode(View view) {
        int resource = 0;
        if (view == imgProfileCode) {
            resource = R.drawable.img_profile_code;
        } else if (view == imgCameraCode) {
            resource = R.drawable.img_camera_code;
        } else if (view == imgGalleryCode) {
            resource = R.drawable.img_gallery_code;
        }
        ImageViewerDialog
                .newInstance(resource)
                .show(getSupportFragmentManager(), "");
    }

    public void showImage(View view) {
        Uri uri;
        if (view == imgProfile) {
            uri = mProfileUri;
        } else if (view == imgCamera) {
            uri = mCameraUri;
        } else if (view == imgGallery) {
            uri = mGalleryUri;
        } else {
            uri = null;
        }

        if (uri != null) {
            startActivity(IntentUtils.getUriViewIntent(this, uri));
        }
    }

    public void showImageInfo(View view) {
        Uri uri;
        if (view == imgProfileInfo) {
            uri = mProfileUri;
        } else if (view == imgCameraInfo) {
            uri = mCameraUri;
        } else if (view == imgGalleryInfo) {
            uri = mGalleryUri;
        } else {
            uri = null;
        }

        new AlertDialog.Builder(this)
                .setTitle("Image Info")
                .setMessage(FileUtil.getFileInfo(this, uri))
                .setPositiveButton("Ok", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            // Uri object will not be null for RESULT_OK
            Uri uri = data.getData();

            switch (requestCode) {
                case PROFILE_IMAGE_REQ_CODE:
                    mProfileUri = uri;
                    ImageViewExtensionKt.setLocalImage(imgProfile, uri, true);
                    break;
                case GALLERY_IMAGE_REQ_CODE:
                    mGalleryUri = uri;
                    ImageViewExtensionKt.setLocalImage(imgGallery, uri, false);
                    break;
                case CAMERA_IMAGE_REQ_CODE:
                    mCameraUri = uri;
                    ImageViewExtensionKt.setLocalImage(imgCamera, uri, false);
                    break;
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

}