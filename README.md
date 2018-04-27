# BleScanCoroutines
Easier and safer Bluetooth Low Energy scanning with Kotlin coroutines.

This library is currently work in progress. Please, feel free to give any feedback in the issues.

The goal of this library is to make scanning for Bluetooth Low Energy devices simpler and safer, especially regarding
undocumented behavior about too frequent and too long scans.

## Download

### Gradle instructions
Make sure you have `jcenter()` in the repositories defined in your project's
(root) `build.gradle` file (default for new Android Studio projects).

Add the version of the library to not repeat yourself if you use multiple
artifacts, and make sure their versions are in sync by adding an ext property
into your root project `build.gradle` file:
```groovy
allProjects {
    ext {
        blescancoroutines_version = '0.1.0-alpha1'
    }
}
```
Here are all the artifacts of this library. Just use the ones you need
(yeah, there's only one at the moment):
```groovy
implementation "com.beepiz.blescancoroutines:blescancoroutines-core:$blescancoroutines_version"
```
