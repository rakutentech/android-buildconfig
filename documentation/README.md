# Documentation Generation

Configuration to create doclava javadoc.

## Example 1: 

```groovy
apply from: '../config/documentation/doclava/android.gradle'
// adds `generateDoclava` task to your project
```

## Example 2: Custom Overview

```groovy
// needs to be defined BEFORE `apply` call 😢
project.ext.documentation = [
    javadocOverview: 'path/to/custom/overview.html'
]
apply from: '../config/documentation/doclava/android.gradle'
```

