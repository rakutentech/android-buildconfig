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
* [BuildSrc](buildSrc/README.md)

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
### 1.0 (2017-12-26)
* Add semantic versioning & documentation guidelines
* Move all configurations into folders
* Split up README into sub-READMEs and move to respective folders
* Merge Kotlin into master