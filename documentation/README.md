# Documentation Generation

Configuration to create doclava javadoc or dokka kdoc.

## For Doclava
### Documentation for Java projects

Example 1:

```groovy
apply from: '../config/documentation/doclava/android.gradle'
// adds `generateDoclava` task to your project
```

Example 2: Customizations

```groovy
// needs to be defined BEFORE `apply` call to doclava script 😢
project.ext.documentation = [
    javadocOverview: 'path/to/custom/overview.html',
    classpath: [ // anything that can be resolved by Project#files
        'path/to/additional/classpath', 
        configurations.javadocClasspath.files as List
    ], // see https://docs.gradle.org/current/javadoc/org/gradle/api/Project.html#files-java.lang.Object...-
    source: { // closue that evaluates to a FileTree object, will be invoked after the project is evaluated, with no arguments
        fileTree(project(':subproject-a').android.sourceSets.main.java.srcDirs[0]) + 
        fileTree(project(':subproject-b').android.sourceSets.main.java.srcDirs[0])
    }
]
apply from: '../config/documentation/doclava/android.gradle'
```

## Dokka

### Documentation for Kotlin projects
```groovy
// root script
buildscript {
  ext.dokka_version = '0.10.0'

  dependencies {
    classpath "org.jetbrains.dokka:dokka-gradle-plugin:${dokka_version}"
  }
}

// sub project
apply from: '../config/documentation/dokka/android.gradle'
dokka {
  configuration {
    sourceRoot {
      path = "{path to source}"
    }
  }
}
```
