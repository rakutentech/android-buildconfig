# Android Library & Applications Default Configurations
To reduce the code duplication there are default configurations for library and application projects:
* `android/library.gradle`
* `android/application.gradle`

```groovy
apply from: '../config/android/library.gradle'
// you can stil overwrite the defaults, see https://google.github.io/android-gradle-dsl/current/
android {
    defaultConfig {
        resValue 'string', 'analytics__version', project.version
        consumerProguardFiles 'proguard-rules.txt'
    }
  resourcePrefix 'analytics_'
}
```