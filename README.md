# Shared Build Configuration for Android libraries
This repository contains common configuration to be used across our Android libraries. The advantage of sharing these configurations is mainly consistency of configuration among multiple libraries and reduction of boilerplate.

## Kickstart
First add the submodule to your project:

```sh
$ cd my_project
$ git submodule add git@github.com:rakutentech/android-buildconfig.git config
```

Then modify your root `build.gradle` to apply the configuration:

```groovy
buildscript {
  // This must be the first line of your buildscript closure
  apply from: "config/index.gradle"

  // …repositories, classpaths, etc…
}
```

From there, you can reference the global `CONFIG` object from any gradle file inside your project.

By default only [versions](versions/README.md) is added to the project. You can apply configurations as desired.

## Available configurations
* [Versions](versions/README.md)
* [Android project defaults](android/README.md)
* [Quality Tools](quality/README.md)
  - Checkstyle
  - Spotbugs
  - Jacoco
  - PMD
  - Detekt
* [BuildSrc](buildSrc/README.md)
* [Documentation](documentation/README.md)
* [Publish](publish/README.md)
* [CircleCI Orbs](circleci/README.md)

## Versioning Contract
This configuration's versions follow [semantic versioning](https://semver.org/). We align all libraries that we bundle together on the same major version of this configuration. To clarify the semantics in the context of shared configurations here are a few examples of what are breaking changes (major version change), backwards compatible improvements (minor version change) and bug fixes (patch level):

Major | Minor | Patch
----- | ----- | ------
move gradle file to a different path | add new configuration | change minor or patch version of dependency
remove gradle file | add new task in existing configuration | refactor
change major version of dependencies | add new dependency version |
change of the versioning contract | reduce boilerplate for consumer | 

To ensure usability we follow these rules:
* Every version has a corresponding git tag of the format `Major.Minor.Patch`
* Every version must describe the changes in [versions section](#versions)
* Major version changes must also provide a migration guide from the previous major version

## Versions <a name="versions"></a>
### 7.0.0
* Update versions:
  * AGP 4.0.2 (requires Gradle 6.1.1+).
  * Targets (and builds with) SDK 30.
* **Updated:** Support for Dokka v1.6.10. This is a BREAKING change. You will need to update your Dokka version and change your dokka configuration as described in the [documentation README](documentation/README.md)

### 6.2.0 (2021-10-27)
* **Feature:** Updated `compile` and `target` to API 31, and min to API 23 in `versions.gradle`

### 6.1.0 (2021-03-05)
* **Feature:** Added publication configuration for Java libraries.

### 6.0.0 (2021-03-03)
* **Feature:** Added Maven Central publishing script.
* **Removed:** Bintray/JCenter publishing script was removed because these services are being [shutdown](https://jfrog.com/blog/into-the-sunset-bintray-jcenter-gocenter-and-chartcenter/).
* **Updated:** Target and compile versions bumped to 30.
* **Updated:** AGP bumped to 4.0.2. This now requires your project to use Gradle 6.1.1+.

### 5.0.0 (2020-11-16)
* Quality: replaced deprecated `FindBugs` with `SpotBugs`.

### 4.3.2 (2020-10-05)
* **Fixed:** GitHub pages publishing would sometimes upload the incomplete documentation.

### 4.3.1 (2020-09-03)
* **Fixed:** Bintray publishing was failing due to an impcompatibility of the Bintray Gradle plugin with the Android Maven Publish plugin. The Bintray Gradle plugin has been removed so you no longer need to add `com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4` as a plugin dependency.

### 4.3.0 (2020-08-06)
* Added config for [creating html version of the userguide markdown](documentation/README.md) and adding it to the generated javadoc.

### 4.2.0 (2020-07-31)
* Added config for [publishing documentation to gh-pages](publish/documentation/README.md)

### 4.1.0 (2020-07-22)
* Added support for using Gradle Property for repository details to handle different settings for snapshot and release repo.

### 4.0.0 (2020-07-17)
* **Breaking Change:** Remove `digital.wup.android-maven-publish` plugin as it is deprecated and AGP now has built-in support for the [maven-publish plugin](https://developer.android.com/studio/build/maven-publish-plugin). Due to this change, you now must configure your publications within a the `afterEvaluate` phase. See the [publishing readme](publish/README.md] for more details. You should also remove `digital.wup.android-maven-publish` from your dependencies.
* Added support for [KDoc generation](documentation/README.md] using dokka.
* Update target/compile SDK to 29 and update Min SDK to 21.
* Update Android Gradle Plugin to 3.6.2. You must now update your Gradle version to 5.6.4+.
* Add support for publishing to [Bintray or Artifactory](publish/README.md).
* Fixed an issue with documentation generation where images sometimes weren't copied to the documentation folder.
* Add `property` closure to `CONFIG`. This can be used to return a value that has been set as either an environment variable or gradle property. Use like this: `CONFIG.property('PROPERTY_NAME')`.

### 3.0.0 (2018-04-13)
* Remove Kotlin check style tool, `Ktlint`, to replace it by `Detekt`.

### 2.1.0 (2018-04-13)
* Quality/jacoco: get rid of dependency of old unmaintained gradle plugin.

### 2.0.0 (2018-03-26)
* Documentation/doclava: make image assets project local, i.e. not shared among subprojects.

### 1.2.0 (2018-03-23)
* Documentation/doclava: add customization of javadoc `source`
* Documentation/doclava: add customization of javadoc `classpath`
* Documentation/doclava: simplified javadoc classpath setup
* buildSrc/SnapshotCheck: only apply to projcets in release version (according to semver)
* Quality/Pmd: Exclude `AvoidFieldNameMatchingMethodName`, `JUnitTestContainsTooManyAsserts`, `CommentDefaultAccessModifier`, `MethodArgumentCouldBeFinal` rules
* Quality/Checkstyle: Add support for `@SuppressWarnings("checkstyle:RuleName)` suppression of checkstyle rules
* Quality/Findbugs: Setup complete classpath for findbugs task, relying on `android.libraryVariants`. Note that this does not work on android applications, but can be adapted to that usecase if it becomes necessary 

### 1.1.0 (2018-01-16)
* Documantation: Add doclava javadoc generation
* Quality/Checkstyl: Change checkstyle severity from `warning` to `error` so that non-compliant code fails the build (can still be disabled with `ignoreFailues` flag in quality task extensions)
* Quality/jacocco: automatically make `check` depend on `jacacoTestReport`

### 1.0.0 (2017-12-26)
* Add semantic versioning & documentation guidelines
* Move all configurations into folders
* Split up README into sub-READMEs and move to respective folders
* Merge Kotlin into master
