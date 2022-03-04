# Documentation Generation

Configuration to create dokka kdoc or doclava javadoc.

## For Dokka

### Documentation for Kotlin projects
```groovy
// root script
buildscript {
  ext.dokka_version = '1.6.10'

  dependencies {
    classpath "org.jetbrains.dokka:dokka-gradle-plugin:${dokka_version}"
  }
}

// sub project
apply from: '../config/documentation/dokka/android.gradle'
dokkaGfm.configure {
    dokkaSourceSets {
        named("{source name i.e. 'main'}") {
            sourceRoots.from(file("{path to source}"))
        }
    }
}
```

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
    },
    failOnError: true
]
apply from: '../config/documentation/doclava/android.gradle'
```

### Adding User Guide to Javadoc

This compiles a markdown file to html and adds it as `USERGUIDE-final.html` to the generated javadoc from the Doclava task.

```groovy
// the user guide gradle file needs to be included first!
apply from: '../config/documentation/userguide/userguide.gradle'
apply from: '../config/documentation/doclava/android.gradle'
```

#### Customization

Make sure that the `javadocOverview` from `userguide.gradle` will be set to the `project.ext.documentation` if customization is done for `doclava`.

```groovy
apply from: '../config/documentation/userguide/userguide.gradle'
def overview = project.ext.documentation.javadocOverview

project.ext.documentation = [
    javadocOverview: overview,
    classpath: [ ... ],
    source: { ... },
    failOnError: true
]
apply from: '../config/documentation/doclava/android.gradle'
```