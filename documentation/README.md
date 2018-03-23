# Documentation Generation

Configuration to create doclava javadoc.

## Example 1: 

```groovy
apply from: '../config/documentation/doclava/android.gradle'
// adds `generateDoclava` task to your project
```

## Example 2: Customizations

```groovy
// needs to be defined BEFORE `apply` call 😢
project.ext.documentation = [
    javadocOverview: 'path/to/custom/overview.html',
    classpath: [ // anything that can be resolved by Project#files
        'path/to/additional/classpath', 
        configurations.javadocClasspath.files as List
    ] // see https://docs.gradle.org/current/javadoc/org/gradle/api/Project.html#files-java.lang.Object...-
]
apply from: '../config/documentation/doclava/android.gradle'
```

