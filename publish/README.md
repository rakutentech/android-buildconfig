# Publishing

These configurations can be used for publishing AARs to [Bintray](https://bintray.com/) or an [Artifactory](https://jfrog.com/artifactory/) Repo.

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

You will need to add the [Maven Publish Plugin](https://github.com/wupdigital/android-maven-publish) and [Bintray Plugin](https://github.com/bintray/gradle-bintray-plugin) to your classpath in your project `build.gradle`.

```groovy
bulidscript {
  dependencies { // see https://github.com/wupdigital/android-maven-publish
    classpath 'digital.wup:android-maven-publish:3.6.2'
    classpath 'com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4'
  }
}
```

### 3. Apply publishing scripts

Next you can apply either `config/publish/bintray.gradle` or `config/publish/artifactory.gradle` in the `build.gradle` file for the module you wish to publish. If you are using Bintray's [OSS Jfrog Artifactory](https://oss.jfrog.org/) for snapshots, you can apply the Artifactory script in the case that you're publishing a snapshot version.

```
apply from: '../config/publish/android.gradle'
publishing {
  publications {
    myAndroidLibraryName(MavenPublication, androidArtifact())
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

## Configuration

You can overwrite the default values of a publication by passing in a map, e.g.

```groovy
apply from: '../config-internal/publish/android.gradle'
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
      scmUrl:       'https://www.example.com/repo.git'
    ))
  }
}
```
