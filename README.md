# About this Fork

I've forked this project to migrate to new [Android Activity Result APIs](https://developer.android.com/training/basics/intents/result).  
[![](https://jitpack.io/v/GuilhE/ImagePicker.svg)](https://jitpack.io/#GuilhE/ImagePicker)

Usage:
```kotlin
private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
    if (it.resultCode == Activity.RESULT_OK) {
        //you're business logic
    }
}

//If you want both Camera and Gallery
ImagePicker.with(this)
    //...
    .createIntentFromDialog { launcher.launch(it) }

//If you want just one option
launcher.launch(
    ImagePicker.with(this)
        //...
        .cameraOnly() // or galleryOnly()
        .createIntent()
)
```
To use camera in Android 11 ([package visibility](https://developer.android.com/about/versions/11/privacy/package-visibility)) don't forget to add this:
```xml
<manifest package="com.example">
    <queries>
        <intent>
            <action android:name="android.media.action.IMAGE_CAPTURE" />
        </intent>
    </queries>
    ...
</manifest>
```

See the original repository README [here](https://github.com/Dhaval2404/ImagePicker)!

# Changelog
## Fork 2.0.1
- Removed `fun saveDir(path: String): Builder` and `fun saveDir(file: File): Builder`

---

## Fork 2.0.0
- `CompressionProvider.kt` logic refactor
- Removed `fun compress(maxSize: Int): Builder` since it was comparing the size of the file and not the image (and the resize operation is enough);
- Image maxResultSize (resize) algorithm refactor: `fun maxResultSize(width: Int, height: Int, keepRatio: Boolean = false): Builde`;
- Replaced AsyncTask with coroutines

---

## Fork 1.9.1
- Removed all `sstartActivityForResult` from library since caller is responsible to provide an `ActivityResultLauncher<Intent>`
- Removed https://github.com/florent37/InlineActivityResult dependency

---

## Fork 1.9.0
- Added [Android Activity Result APIs](https://developer.android.com/training/basics/intents/result)
