# 📸Image Picker Library for Android

[![Download](https://api.bintray.com/packages/dhaval2404/maven/imagepicker/images/download.svg) ](https://bintray.com/dhaval2404/maven/imagepicker/_latestVersion) 
[![Releases](https://img.shields.io/github/release/dhaval2404/imagePicker/all.svg?style=flat-square)](https://github.com/Dhaval2404/ImagePicker/releases)
[![API](https://img.shields.io/badge/API-19%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=19)
![Language](https://img.shields.io/badge/language-Kotlin-orange.svg)
[![Android Arsenal]( https://img.shields.io/badge/Android%20Arsenal-ImagePicker-green.svg?style=flat )]( https://android-arsenal.com/details/1/7510 )
[![PRWelcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](https://github.com/Dhaval2404/ImagePicker)
[![Say Thanks!](https://img.shields.io/badge/Say%20Thanks-!-1EAEDB.svg)](https://saythanks.io/to/Dhaval2404)
[![Twitter](https://img.shields.io/twitter/url/https/github.com/Dhaval2404/ImagePicker.svg?style=social)](https://twitter.com/intent/tweet?text=Check+out+an+ImagePicker+library+to+Pick+an+image+from+the+Gallery+or+Capture+an+image+with+Camera.+https%3A%2F%2Fgithub.com%2FDhaval2404%2FImagePicker+%40dhaval2404+%23Android+%23Kotlin+%23AndroidDev)

<div align="center">
  <sub>Built with ❤︎ by
  <a href="https://twitter.com/Dhaval2404">Dhaval Patel</a> and
  <a href="https://github.com/dhaval2404/imagepicker/graphs/contributors">
    contributors
  </a>
</div>
<br/>

Simple Library to **Pick an image from the Gallery or Capture image using Camera**. It also allows to **Crop and Compress the Image based on resolution and image size**.

Almost 90% of the app that I have developed has Image upload feature. To simplify the image pick/capture option I have created this library. Its easily configurable and easy to use.

# 💻Usage


1. Gradle dependency:

	```groovy
	allprojects {
	   repositories {
	      	jcenter()
           	maven { url "https://jitpack.io" }  //Make sure to add this in your project for uCrop
	   }
	}
	```

    ```groovy
   implementation 'com.github.dhaval2404:imagepicker:1.5'
    ```
    
   **If you are yet to Migrate on AndroidX, Use support build artifact:**
   ```groovy
   implementation 'com.github.dhaval2404:imagepicker-support:1.5'
    ```

    **If you want to get the activity result inline in a modern way (lambda) install [InlineActivityResult](https://github.com/florent37/InlineActivityResult) library:**
   ```groovy
   implementation 'com.github.florent37:inline-activity-result-kotlin:1.0.1'
    ```
    
2.  <span style="color:red">**If you target Android 10 or higher(targetSdkVersion >= 29)**</span>, set the value of ``requestLegacyExternalStorage`` to true in your app's manifest file:

      ```xml
    <manifest ... >
          <!-- This attribute is "false" by default on apps targeting
               Android 10 or higher. -->
          <application android:requestLegacyExternalStorage="true" ... >
            ...
          </application>
    </manifest>
      ```

3. The ImagePicker configuration is created using the builder pattern.

	**Kotlin**
    
	```kotlin
    ImagePicker.with(this)
            .crop()	    			//Crop image(Optional), Check Customization for more option
            .compress(1024)			//Final image size will be less than 1 MB(Optional)
            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    ```
    
    **Java**
    
    ```kotlin
    ImagePicker.Companion.with(this)
            .crop()	    			//Crop image(Optional), Check Customization for more option
            .compress(1024)			//Final image size will be less than 1 MB(Optional)
            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    ```
    
4. Handling results

    
    **Default method**
    Override `onActivityResult` method and handle ImagePicker result.

    ```kotlin
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         super.onActivityResult(requestCode, resultCode, data)
         if (resultCode == Activity.RESULT_OK) {
             //Image Uri will not be null for RESULT_OK
             val fileUri = data?.data
             imgProfile.setImageURI(fileUri)
          
            //You can get File object from intent
            val file:File = ImagePicker.getFile(data)
           
            //You can also get File Path from intent
            val filePath:String = ImagePicker.getFilePath(data)     
         } else if (resultCode == ImagePicker.RESULT_ERROR) {
             Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
         } else {
             Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
         }
    }
    ```

    **Inline method (with InlineActivityResult library)**
    ```kotlin
    ImagePicker.with(this)
            .crop(1f, 1f)               //Crop Square image(Optional)
            .compress(1024)         //Final image size will be less than 1 MB(Optional)
            .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
            .start { resultCode, data ->
                if (resultCode == Activity.RESULT_OK) {
                    //Image Uri will not be null for RESULT_OK
                     val fileUri = data?.data
                     imgProfile.setImageURI(fileUri)
                  
                    //You can get File object from intent
                    val file:File = ImagePicker.getFile(data)
                   
                    //You can also get File Path from intent
                    val filePath:String = ImagePicker.getFilePath(data)
                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
    ```

# 🎨Customization

 *  Pick image using Gallery

	```kotlin
	ImagePicker.with(this)
		.galleryOnly()	//User can only select image from Gallery
		.start()	//Default Request Code is ImagePicker.REQUEST_CODE
    ```

 *  Capture image using Camera

	```kotlin
	ImagePicker.with(this)
		.cameraOnly()	//User can only capture image using Camera
		.start()
    ```
 *  Crop image
 		
    ```kotlin
    ImagePicker.with(this)
		.crop()	    //Crop image and let user choose aspect ratio.
		.start()

 *  Crop image with fixed Aspect Ratio
 		
    ```kotlin
    ImagePicker.with(this)
		.crop(16f, 9f)	//Crop image with 16:9 aspect ratio
		.start()
		
    ```            
 *  Crop square image(e.g for profile)
 
     ```kotlin
     ImagePicker.with(this)
		    .cropSquare()	//Crop square image, its same as crop(1f, 1f)
		    .start()
    ```
 *  Compress image size(e.g image should be maximum 1 MB)
		
	```kotlin
    ImagePicker.with(this)
		.compress(1024)	//Final image size will be less than 1 MB
		.start()
    ```
 *  Set Resize image resolution
 		
    ```kotlin
    ImagePicker.with(this)
		.maxResultSize(620, 620)	//Final image resolution will be less than 620 x 620	   
		.start()
    ```
 *  You can also specify the request code with ImagePicker
 		
    ```kotlin
    ImagePicker.with(this)
		.maxResultSize(620, 620)	   
		.start(101)	//Here 101 is request code, you may use this in onActivityResult
    ```    
 
 *  Add Following parameters in your **colors.xml** file, If you want to customize uCrop Activity.
    
    ```xml
    <resources>
        <!-- Here you can add color of your choice  -->
        <color name="ucrop_color_toolbar">@color/teal_500</color>
        <color name="ucrop_color_statusbar">@color/teal_700</color>
        <color name="ucrop_color_widget_active">@color/teal_500</color>
    </resources>    
    ```
    
# 💥Compatibility
  
  * Library - Android Kitkat 4.4+ (API 19)
  * Sample - Android Kitkat 4.4+ (API 19)
  
# ✔️Changelog

### Version: 1.5

  * Fixed app crash issue, due to Camera Permission in manifest [#34](https://github.com/Dhaval2404/ImagePicker/issues/34)
  * Added Option for Dynamic Crop Ratio. Let User choose aspect ratio [#36](https://github.com/Dhaval2404/ImagePicker/issues/36) (Special Thanks to [Dor-Sloim](https://github.com/Dor-Sloim))


### Version: 1.4

  * Optimized Uri to File Conversion (Inspired by [Flutter ImagePicker](https://github.com/flutter/plugins/tree/master/packages/image_picker))
  * Removed redundant CAMERA permission [#26](https://github.com/Dhaval2404/ImagePicker/issues/26) (Special Thanks to [PerrchicK](https://github.com/PerrchicK))

### Version: 1.3

  * Sample app made compatible with Android Kitkat 4.4+ (API 19)
  * Fixed Uri to File Conversion issue [#8](https://github.com/Dhaval2404/ImagePicker/issues/8) (Special Thanks to [squeeish](https://github.com/squeeish))

### Version: 1.2

  * Added Support for Inline Activity Result(Special Thanks to [soareseneves](https://github.com/soareseneves))
  * Fixed issue [#6](https://github.com/Dhaval2404/ImagePicker/issues/6)
  
### Version: 1.1

  * Optimized Compression Logic
  * Replace white screen with transparent one.

### Version: 1.0

  * Initial Build

## 📃 Libraries Used
* uCrop [https://github.com/Yalantis/uCrop](https://github.com/Yalantis/uCrop)
* Compressor [https://github.com/zetbaitsu/Compressor](https://github.com/zetbaitsu/Compressor)
* InlineActivityResult [https://github.com/florent37/InlineActivityResult](https://github.com/florent37/InlineActivityResult)

### Let us know!

We'll be really happy if you sent us links to your projects where you use our component. Just send an email to **dhavalpatel244@gmail.com** And do let us know if you have any questions or suggestion regarding the library.

## License

    Copyright 2019, The Android Open Source Project

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    
