# ✔️Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [2.1] - 2021-05-27
### Added
  * Added uzbekistan translation (Special Thanks to Khudoyshukur Juraev)
### Changed
  * Removed requestLegacyExternalStorage flag
  * Removed unused string resources

## [2.0] - 2021-05-15
### Added
  * Added arabic translation [#157](https://github.com/Dhaval2404/ImagePicker/pull/157)  (Special Thanks to [zhangzhu95](https://github.com/zhangzhu95))
  * Added norwegian translation [#163](https://github.com/Dhaval2404/ImagePicker/pull/163) (Special Thanks to [TorkelV](https://github.com/TorkelV))
  * Added german translation [#192](https://github.com/Dhaval2404/ImagePicker/pull/192) (Special Thanks to [MDXDave](https://github.com/MDXDave))
  * Added method to return Intent for manual launching ImagePicker [#182](https://github.com/Dhaval2404/ImagePicker/pull/182) (Special Thanks to [tobiasKaminsky](https://github.com/tobiasKaminsky))
  * Added support for android 11 [#199](https://github.com/Dhaval2404/ImagePicker/issues/199)
### Changed
  * Fixed Playstore requestLegacyExternalStorage flag issue [#199](https://github.com/Dhaval2404/ImagePicker/issues/199)
  * Fixed android scope storage issue [#29](https://github.com/Dhaval2404/ImagePicker/issues/29)
  * Removed storage permissions [#29](https://github.com/Dhaval2404/ImagePicker/issues/29)
  * Fixed calculateInSampleSize leads to overly degraded quality [#152](https://github.com/Dhaval2404/ImagePicker/issues/152) (Special Thanks to [FlorianDenis](https://github.com/FlorianDenis))
  * Fixed camera app not found issue [#162](https://github.com/Dhaval2404/ImagePicker/issues/162)

## [1.8] - 2020-12-22
### Added
  * Added dialog dismiss listener (Special Thanks to [kibotu](https://github.com/kibotu))
  * Added text localization (Special Thanks to [yamin8000](https://github.com/yamin8000) and Jose Bravo)
### Changed
  * Fixed crash issue on missing camera app [#69](https://github.com/Dhaval2404/ImagePicker/issues/69)
  * Fixed issue selecting images from download folder [#86](https://github.com/Dhaval2404/ImagePicker/issues/86)
  * Fixed exif information lost issue [#121](https://github.com/Dhaval2404/ImagePicker/issues/121)
  * Fixed crash issue on large image crop [#122](https://github.com/Dhaval2404/ImagePicker/issues/122)
  * Fixed saving image in cache issue [#127](https://github.com/Dhaval2404/ImagePicker/issues/127)

## [1.7.5] - 2020-08-30
### Changed
  * Added Polish text translation [#115](https://github.com/Dhaval2404/ImagePicker/issues/115) (Special Thanks to [MarcelKijanka](https://github.com/MarcelKijanka))
  * Failed to find configured root exception [#116](https://github.com/Dhaval2404/ImagePicker/issues/116)

## [1.7.4] - 2020-08-02
### Changed
  * Fixed PNG image saved as JPG after compress issue [#105](https://github.com/Dhaval2404/ImagePicker/issues/105)

## [1.7.3] - 2020-07-18
### Changed
  * Fixed PNG image saved as JPG after crop issue [#94](https://github.com/Dhaval2404/ImagePicker/issues/94)

## [1.7.2] - 2020-07-14
### Changed
  * Fixed .crop() opening gallery or camera twice [#32](https://github.com/Dhaval2404/ImagePicker/issues/32)
  * Fixed UCropActivity Crash Android 4.4 (KiKat) [#82](https://github.com/Dhaval2404/ImagePicker/issues/82)

## [1.7.1] - 2020-03-26
### Changed
  * Fixed The application could not be installed: INSTALL_FAILED_CONFLICTING_PROVIDER issue [#67](https://github.com/Dhaval2404/ImagePicker/issues/67)

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
  * Retrieve Image Result as File, File Path as String or Uri object

[Unreleased]: https://github.com/Dhaval2404/ImagePicker/compare/v2.2...HEAD
[2.1]: https://github.com/Dhaval2404/ImagePicker/compare/v2.0...v2.1
[2.0]: https://github.com/Dhaval2404/ImagePicker/compare/v1.8...v2.0
[1.8]: https://github.com/Dhaval2404/ImagePicker/compare/v1.7.5...v1.8
[1.7.5]: https://github.com/Dhaval2404/ImagePicker/compare/v1.7.4...v1.7.5
[1.7.4]: https://github.com/Dhaval2404/ImagePicker/compare/v1.7.3...v1.7.4
[1.7.3]: https://github.com/Dhaval2404/ImagePicker/compare/v1.7.2...v1.7.3
[1.7.2]: https://github.com/Dhaval2404/ImagePicker/compare/v1.7.1...v1.7.2
[1.7.1]: https://github.com/Dhaval2404/ImagePicker/compare/v1.7...v1.7.1
[1.7]: https://github.com/Dhaval2404/ImagePicker/compare/v1.6...v1.7
[1.6]: https://github.com/Dhaval2404/ImagePicker/compare/v1.5...v1.6
[1.5]: https://github.com/Dhaval2404/ImagePicker/compare/v1.4...v1.5
[1.4]: https://github.com/Dhaval2404/ImagePicker/compare/v1.3...v1.4
[1.3]: https://github.com/Dhaval2404/ImagePicker/compare/v1.2...v1.3
[1.2]: https://github.com/Dhaval2404/ImagePicker/compare/v1.1...v1.2
[1.1]: https://github.com/Dhaval2404/ImagePicker/compare/v1.0...v1.1
[1.0]: https://github.com/Dhaval2404/ImagePicker/tree/v1.0
  
