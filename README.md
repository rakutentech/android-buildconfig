# Shared Build Configuration for Android libraries
This repository contains common configuration to be used across our Android libraries. The advantage of sharing these configurations in mainly consistency of configuration among multiple libraries and reduction of boilerplate.

## Kickstart
First add the subomudle to your project:

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
  - Findbugs
  - Jacoco
  - PMD
  - Detekt
* [BuildSrc](buildSrc/README.md)
* [Documentation](documentation/README.md)

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

## Migration 1.x.x -> 2.0.0
If you use `docummentation/doclava` with image assets: move `$rootProject/images` to the project that includes the `documentation/doclava/android.gradle` script.

## Versions <a name="versions"></a>
### HEAD
* Update versions:
  * AGP 3.1.0 (requires Gradle 4.4+).
  * Targets (and builds with) SDK 27.

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
