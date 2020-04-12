# ✔️Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.7] - 2020-03-23
### Changed
  * Added option to limit MIME types while choosing a gallery image (Special Thanks to [Marchuck](https://github.com/Marchuck))
  * Introduced ImageProviderInterceptor, Can be used for analytics (Special Thanks to [Marchuck](https://github.com/Marchuck))
  * Fixed FileProvider of the library clashes with the FileProvider of the app [#51](https://github.com/Dhaval2404/ImagePicker/issues/51) (Special Thanks to [OyaCanli](https://github.com/OyaCanli))
  * Added option to set Storage Directory [#52](https://github.com/Dhaval2404/ImagePicker/issues/52)
  * Fixed NullPointerException in FileUriUtils.getPathFromRemoteUri()  [#61](https://github.com/Dhaval2404/ImagePicker/issues/61) (Special Thanks to [himphen](https://github.com/himphen))

## [1.6] - 2020-01-06
### Changed
  * Improved UI/UX of sample app
  * Removed Bitmap Deprecated Property [#33](https://github.com/Dhaval2404/ImagePicker/issues/33) (Special Thanks to [nauhalf](https://github.com/nauhalf))
  * Camera opens twice when "Don't keep activities" option is ON [#41](https://github.com/Dhaval2404/ImagePicker/issues/41) (Special Thanks to [benji101](https://github.com/benji101))
  * Fixed uCrop Crash Issue [#42](https://github.com/Dhaval2404/ImagePicker/issues/42)

## [1.5] - 2019-10-14
### Added
  * Added Option for Dynamic Crop Ratio. Let User choose aspect ratio [#36](https://github.com/Dhaval2404/ImagePicker/issues/36) (Special Thanks to [Dor-Sloim](https://github.com/Dor-Sloim))
### Changed
  * Fixed app crash issue, due to Camera Permission in manifest [#34](https://github.com/Dhaval2404/ImagePicker/issues/34)

## [1.4] - 2019-09-03
### Changed
  * Optimized Uri to File Conversion (Inspired by [Flutter ImagePicker](https://github.com/flutter/plugins/tree/master/packages/image_picker))
### Removed
  * Removed redundant CAMERA permission [#26](https://github.com/Dhaval2404/ImagePicker/issues/26) (Special Thanks to [PerrchicK](https://github.com/PerrchicK))

## [1.3] - 2019-07-24
### Added
  * Sample app made compatible with Android Kitkat 4.4+ (API 19)
### Changed
  * Fixed Uri to File Conversion issue [#8](https://github.com/Dhaval2404/ImagePicker/issues/8) (Special Thanks to [squeeish](https://github.com/squeeish))

## [1.2] - 2019-05-13
### Added
  * Added Support for Inline Activity Result(Special Thanks to [soareseneves](https://github.com/soareseneves))
### Changed
  * Fixed issue [#6](https://github.com/Dhaval2404/ImagePicker/issues/6)
  
## [1.1] - 2019-04-02
### Changed
  * Optimized Compression Logic
  * Replace white screen with transparent one.

## [1.0] - 2019-02-11
### Added
  * Pick Gallery Image
  * Capture Camera Image
  * Crop Image(Its based on [uCrop](https://github.com/Yalantis/uCrop))
  * Compress Image(Compress image based on resolution and size)
  * Handle Runtime Permission for Camera and Storage
  * Retrive Image Result as File, File Path as String or Uri object

[Unreleased]: https://github.com/Dhaval2404/ImagePicker/compare/v1.7...HEAD
[1.7]: https://github.com/Dhaval2404/ImagePicker/compare/v1.6...v1.7
[1.6]: https://github.com/Dhaval2404/ImagePicker/compare/v1.5...v1.6
[1.5]: https://github.com/Dhaval2404/ImagePicker/compare/v1.4...v1.5
[1.4]: https://github.com/Dhaval2404/ImagePicker/compare/v1.3...v1.4
[1.3]: https://github.com/Dhaval2404/ImagePicker/compare/v1.2...v1.3
[1.2]: https://github.com/Dhaval2404/ImagePicker/compare/v1.1...v1.2
[1.1]: https://github.com/Dhaval2404/ImagePicker/compare/v1.0...v1.1
[1.0]: https://github.com/Dhaval2404/ImagePicker/tree/v1.0
  
