# Shared Versions

To keep all our libraries' transitive dependecies in sync we use this shared version configuration. We try to avoid transitive dependency versions conflicts as much as possible to make integration of our SDKs as easy as possible.

## What's provided

* `CONFIG.configDir` exports the path to the config folder.
* `CONFIG.versions` exports version information for runtime dependendencies and target environment across SDK components.
* `jitpack()` and `gradle()` aliases in repositories closure (gradle plugin repo only works for 
`buildscript.repositories` closure).

## Example

```groovy
buildscript {
  apply from: "config/index.gradle"
  repositories {
    jcenter()
    jitpack()
    gradle()
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