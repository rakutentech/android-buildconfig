# Quality Tools
These are default configurations for [checkstyle](https://github.com/checkstyle/checkstyle), [pmd](https://github.com/pmd/pmd), [findbugs](https://github.com/findbugsproject/findbugs) and [jacoco](https://github.com/jacoco/jacoco), they do the following:
* configure a project to use the tool by including a single script
* register a task that will run as part of the `check` task
```groovy
// android plugin -  applies all checkstyle, pmd, findbugs and jacoco to android projects
apply from: '../config/quality/android.gradle'
// java plugin - applies only checkstyle to java projects (TODO: add others)
apply from: '../config/quality/java.gradle'
```

Alternative you can apply the tool configurations individually.

### Checkstyle
Rules for the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) are provided in `checkstyle/checkstyle.xml`.
To make Android Studio aware of the rules, you'll need to install the [CheckStyle-IDEA plugin](https://plugins.jetbrains.com/plugin/1065-checkstyle-idea).
```groovy
// java plugin
apply from: '../config/quality/checkstyle/java.gradle'
// android plugin
apply from: '../config/quality/checkstyle/android.gradle'

// optional: extra configuration options, see https://docs.gradle.org/current/dsl/org.gradle.api.plugins.quality.CheckstyleExtension.html
checkstyle {
    ignoreFailures false
}
```

### PMD
```groovy
// android plugin
apply from: '../config/quality/pmd/android.gradle'

// optional: extra configuration options, see https://docs.gradle.org/current/dsl/org.gradle.api.plugins.quality.PmdExtension.html
pmd {
  consoleOutput = false
}
```

### Findbugs
```groovy
// android plugin
apply from: '../config/quality/findbugs/android.gradle'

// optional: extra configuration options, see https://docs.gradle.org/current/dsl/org.gradle.api.plugins.quality.FindBugsExtension.html
findbugs {
  showProgress = false
}
```

## Jacaco
```groovy
// in android project
apply from: '../config/quality/jacoco/android.gradle'
// opional, see https://docs.gradle.org/current/dsl/org.gradle.testing.jacoco.plugins.JacocoPluginExtension.html
jacoco {
  toolVersion = 'x.y.z'
}
```

## Errorprone (with NullAway)
```groovy
// root script
buildscript {
  apply from: "config/index.gradle"
  repositories {
    mavenCentral()
  }
  dependencies {
    classpath 'net.ltgt.gradle:gradle-errorprone-plugin:0.0.13'
  }
}
// sub project
apply from: '../config/quality/errorprone/android.gradle'
// optional: extra configuration options, see https://github.com/tbroyer/gradle-errorprone-plugin and http://errorprone.info/docs/flags
tasks.withType(JavaCompile) {
    options.compilerArgs += [ '-Xep:DeadException:WARN', '-Xep:GuardedByValidator:OFF' ]
}
```