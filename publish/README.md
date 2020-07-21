# Publishing

These configurations can be used for publishing AARs to [Bintray](https://bintray.com/) or an [Artifactory](https://jfrog.com/artifactory/) Repo. This uses the [maven-publish-plugin](https://developer.android.com/studio/build/maven-publish-plugin) which is built-in to AGP 3.6.0+.

## Setup

### 1. Add Environment variables

The following environment variables need to be set.

```bash
# If publishing to Bintray
BINTRAY_USER=username
BINTRAY_KEY=password
BINTRAY_REPO=your_bintray_repo_name
BINTRAY_PACKAGE_NAME=your_bintray_package_name

# If publishing to Artifactory (this example uses the Jfrog OSS Snapshot repo)
ARTIFACTORY_USER=username
ARTIFACTORY_PASSWORD=password
ARTIFACTORY_URL=https://oss.jfrog.org/artifactory/oss-snapshot-local
ARTIFACTORY_REPO=oss-snapshot-local
```

### 2. Add plugins to buildscript

You will need to add the [Bintray Plugin](https://github.com/bintray/gradle-bintray-plugin) to your classpath in your project's `build.gradle`. If you don't plan to publish to Bintray (and only to Artifactory) then you can skip this step.

```groovy
bulidscript {
  dependencies {
    // If publishing to Bintray
    classpath 'com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4'
  }
}
```

### 3. Apply publishing scripts

Next, in the `build.gradle` file for the module you wish to publish, you can apply the `config/publish/android.gradle` script and configure your publications.

Note that you publications must be configured inside the `afterEvaluate` phase. This is because the Android components which will be published aren't create until this phase.

You can also apply either the `config/publish/bintray.gradle` or `config/publish/artifactory.gradle` scripts to configure the repos where your artifacts will be published. If you are using Bintray's [OSS Jfrog Artifactory](https://oss.jfrog.org/) for snapshots, you can apply the Artifactory script in the case that you're publishing a snapshot version.

```groovy
apply from: '../config/publish/android.gradle'
// Must be configured inside afterEvaluate phase because Android components are only available here
afterEvaluate {
    publishing {
      publications {
        myAndroidLibraryName(MavenPublication, androidArtifact())
      }
    }
}

def isSnapshot = project.version.contains('-')
if (isSnapshot) {
  apply from: '../config/publish/artifactory.gradle'
} else {
  apply from: '../config/publish/bintray.gradle'
}
```

This will do:

* create maven publication for with the name `myAndroidLibraryName`
  + uses `project.name`, `project.group`, `project.version`, `project.description`, `project.url`, `project.licenseName`, `project.licenseUrl`, and `project.scmUrl`
  + The values are set in `gradle.properties`, but can be optionally configured by passing a Map, as shown below.
  + Configures publication for `component.release` as `from` field - your project's `AAR`
  + Add artifacts for the JavaDocs (for Java projects) or KDocs (for Kotlin projects) and a JAR containing your project's source code to your publications.
  + Configure the repos where your artifacts will be published - Artifactory and/or Bintray. This uses the credentials you've set as environment variables.

## Custom Features

### 1. Different repository for snapshot version

You can set different Artifactory URLs for snapshot and release version by setting the `ARTIFACTORY_URL` Gradle Property.

```groovy
def isSnapshot = project.version.contains('-')
if (isSnapshot) {
  project.ext.ARTIFACTORY_URL="https://oss.jfrog.org/artifactory/oss-snapshot-local"
} else {
  project.ext.ARTIFACTORY_URL="https://oss.jfrog.org/artifactory/oss-release-local"
}
apply from: '../config/publish/artifactory.gradle'
```

## Configuration

You can overwrite the default values of a publication by passing in a map, e.g.

```groovy
apply from: '../config/publish/android.gradle'
afterEvaluate {
    publishing {
      publications {
        myJavaLibraryName(MavenPublication, androidArtifact(
          from:         compontents.groovy, // different components source
          artifacts:    [someJarTask], // Additional artifacts
          groupId:      'different-group',
          artifactId:   'different-artifact-id',
          name:         'different-name',
          version:      'different-version',
          url:          'different url',
          description:  'different description',
          licenseName:  'different license',
          licenseUrl:   'https://www.example.com',
          scmUrl:       'https://www.example.com/repo.git',
          excludeSourceJar: false // By default, a Jar containing the module's source in added to artifacts - set to true to exlcude the Jar
        ))
      }
    }
}
```
