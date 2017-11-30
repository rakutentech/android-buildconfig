# Shared Build Configuration for Android SDK modules

This repository contains common configuration to be used across our Android libraries.
It can be included in a repo using any method: git submodule, checked-out copy or subtree merging.

## Kickstart

```sh
$ cd my_project
$ git submodule add git@github.com:rakutentech/android-buildconfig.git config
```

## Build Scripts

Then, modify your root `build.gradle` to apply the configuration:

```groovy
buildscript {
  // This must be the first line of your buildscript closure
  apply from: "config/index.gradle"

  // …repositories, classpaths, etc…
}
```

From there, you can reference the global `CONFIG` object from any gradle file inside your project.

### What's provided

* `CONFIG.configDir` exports the path to the config folder.
* `CONFIG.versions` exports version information for runtime dependendencies and target environment across SDK components. Note that testing dependencies, annotation processors etc are omitted for now, as those only impact the local build environment rather than the consumer applications.

### Example

```groovy
buildscript {
    apply from: "config/index.gradle"

    repositories {
        jcenter()
    }
    dependencies {
        classpath "com.android.tools.build:gradle:${CONFIG.versions.android.plugin}"
    }
}

allprojects {
    repositories {
        jitpack()
    }
}
```

## BuildSrc

Include `config/buildSrc/build.gradle` in your projects `buildSrc/build.gradle`

```groovy
apply from: '../config/buildSrc/build.gradle'
```

### What's provided

* `CheckGradleFilesForSnapshotDependencies` task that will parse all `*.gradle` files for bulidscript classpath dependencies and project compile dependencies for pre release versions (identified by `-` in the version, following [semver.org](http://semver.org/))

### Example

```groovy
import com.rakuten.tech.tool.CheckGradleFilesForSnapshotDependencies
task preReleaseCheck(type: CheckGradleFilesForSnapshotDependencies) {
     exclude = [
             ~/.*\/config\/.*\.gradle/,
             ~/.*\/buildSrc\/.*\.gradle/,
             ~/.*\/TestUI\/.*\.gradle/,
             ]
}
```

## Quality Tools
These are default configurations for [checkstyle](https://github.com/checkstyle/checkstyle), [pmd](https://github.com/pmd/pmd) and [findbugs](https://github.com/findbugsproject/findbugs), they do the following:
* configure a project to use the tool by including a single script
* register a task that will run as part of the `check` task

### Checkstyle
Rules for the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) are provided in `checkstyle/checkstyle.xml`.
To make Android Studio aware of the rules, you'll need to install the [CheckStyle-IDEA plugin](https://plugins.jetbrains.com/plugin/1065-checkstyle-idea).

```groovy
// java plugin
apply from: '../config/quality/checkstyle/java.gradle'
// android plugin
apply from: '../config/quality/checkstyle/android.gradle'

// optional: extra configuration options,  see https://docs.gradle.org/current/dsl/org.gradle.api.plugins.quality.CheckstyleExtension.html
checkstyle {
    ignoreFailures false
}
```

### PMD
```groovy
// android plugin
apply from: '../config/quality/pmd/android.gradle'

// optional: extra configuration options,  see https://docs.gradle.org/current/dsl/org.gradle.api.plugins.quality.PmdExtension.html
pmd {
  consoleOutput = false
}
```

### Findbugs
```groovy
// android plugin
apply from: '../config/quality/findbugs/android.gradle'

// optional: extra configuration options,  see https://docs.gradle.org/current/dsl/org.gradle.api.plugins.quality.FindBugsExtension.html
findbugs {
  showProgress = false
}
```

## Default Configurations

To reduce the code duplication there are 3 default configs:

* `jacoco.gradle`
* `android/library.gradle`
* `android/application.gradle`

Unfortunately you need to put the respective plugin on the classpath yourself 😢. Example


```groovy
// root script
buildscript {
    apply from: "config/index.gradle"
    repositories {
        artifactoryRelease()
        jcenter()
    }
    dependencies {
        classpath 'com.dicedmelon.gradle:jacoco-android:0.1.1'
    }
}
// sub project
apply from: '../config/android/library.gradle'
apply from: '../config/jacoco.gradle'
// you can stil overwrite the defaults, e.g.
android {
    defaultConfig {
        resValue 'string', 'analytics__version', project.version
        consumerProguardFiles 'proguard-rules.txt'
    }
  resourcePrefix 'analytics_'
}
jacocoAndroidUnitTestReport {
    xml.enabled false
}
```
