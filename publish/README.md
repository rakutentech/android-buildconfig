# Publishing

These configurations can be used for publishing AARs to [Maven Central](https://central.sonatype.org/), [Artifactory](https://jfrog.com/artifactory/) Repo, or Github Packages. This uses the [maven-publish-plugin](https://developer.android.com/studio/build/maven-publish-plugin) which is built-in to AGP 3.6.0+.

## Setup

### 1. Add Environment variables for publishing

The following need to be set as either environment variables or Gradle properties.

#### Maven Central

```bash
MAVEN_CENTRAL_USER=username
MAVEN_CENTRAL_PASSWORD=password
MAVEN_CENTRAL_SIGNING_KEY_ID=your_key_id
MAVEN_CENTRAL_SIGNING_PASSWORD=your_signing_password
MAVEN_CENTRAL_SIGNING_KEY_RING_FILE=path/to/your/gpg-private-key.gpg
```

#### Artifactory/ GitHub Packages

```bash
REPOSITORY_USER=username
REPOSITORY_PASSWORD=password
REPOSITORY_URL=https://www.example.com
REPOSITORY_NAME=oss-snapshot-local
```

### 2. Add Gradle properties for configuration

Next, you must add the following properties to your `gradle.properties` file. Alternatively, you can pass these values directly to the configuration script as shown [Advanced Configuration](#advanced-configuration).

```
group = com.example.your.group.id
url = https://www.example.com
description = Description of your library
licenseName = "MIT License"
licenseUrl = https://opensource.org/licenses/MIT
scmUrl = https://example.com/yourproject.git
developerName = YourName
developerEmail = your.email@example.com
developerOrganization = Your Organization
developerOrganizationUrl = https://www.example.com
```

### 3. Configure your publications

Next, in the `build.gradle` file for the module you wish to publish, you can apply the script to configure your publications. You can use either the Android or Java configuration, depending on your library type.

#### Android Library

Note that your publications must be configured inside the `afterEvaluate` phase. This is because the Android components which will be published aren't created until this phase.

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
```

#### Java Library

```groovy
apply from: '../config/publish/java.gradle'
publishing {
  publications {
    myJavaLibraryName(MavenPublication, javaArtifact())
  }
}
```

#### This script will do:

* create maven publication for with the name `myAndroidLibraryName` (or `myJavaLibraryName`)
  + Configures your publication using the meta-data you set in `gradle.properties` in the [above step](#2.-add-gradle-properties-for-configuration).
    + Or this meta-data can be optionally configured by passing a Map, as shown in [Advanced Configuration](#advanced-configuration).
  + Configures publication for `component.release` as `from` field - your project's `AAR`. (Or configures publication for `component.java` in the case of a Java library).
  + Add artifacts for the JavaDocs (for Java projects) or KDocs (for Kotlin projects) and a JAR containing your project's source code to your publications.

### 4. Configure publishing to Maven Central or Artifactory/ GitHub Packages

Next, you can apply either the `config/publish/maven-central.gradle` or `config/publish/repository.gradle` scripts to configure the repos where your artifacts will be published.

#### Maven Central

If you wish to publish snapshots, you can use the `MAVEN_CENTRAL_IS_SNAPSHOT` property if your current version is a snapshot version.

```groovy
def isSnapshot = project.version.contains('-')
if (isSnapshot) {
  ext["MAVEN_CENTRAL_IS_SNAPSHOT"] = true
}
apply from: '../config/publish/maven-central.gradle'
```

#### Artifactory/ GitHub Packages

If you wish to publish snapshots, you can publish to your snapshot URL if your current version is a snapshot version.

```groovy
def isSnapshot = project.version.contains('-')
if (isSnapshot) {
  ext["REPOSITORY_URL"] = System.getenv("REPOSITORY_URL_SNAPSHOT")
} else {
  ext["REPOSITORY_URL"] = System.getenv("REPOSITORY_URL_RELEASE")
}
apply from: '../config/publish/repository.gradle'
```

#### This script will do:

+ Configure the repos where your artifacts will be published - Maven Central or Artifactory/ GitHub Packages.
+ Uses the credentials you've set as environment variables.
+ If Maven Central, configures the PGP signing key as defined in your environment variables.

## Custom Features

### 1. Different repository details for snapshot version

You can set different repository details for snapshot and release version by setting the following Gradle Properties.
* REPOSITORY_NAME
* REPOSITORY_USER
* REPOSITORY_PASSWORD
* REPOSITORY_URL

If the gradle property is not set, the System Environment will be used.

```groovy
def isSnapshot = project.version.contains('-')
if (isSnapshot) {
  project.ext.REPOSITORY_NAME="repo-snapshot-local"
  project.ext.REPOSITORY_USER="snapshot_user"
  project.ext.REPOSITORY_PASSWORD="snapshot_password"
  project.ext.REPOSITORY_URL="https://oss.jfrog.org/artifactory/oss-snapshot-local"
} else {
  project.ext.REPOSITORY_NAME="repo-release-local"
  project.ext.REPOSITORY_USER="release_user"
  project.ext.REPOSITORY_PASSWORD="release_password"
  project.ext.REPOSITORY_URL="https://oss.jfrog.org/artifactory/oss-release-local"
}
apply from: '../config/publish/repository.gradle'
```

## Advanced Configuration

You can overwrite the default values of a publication by passing in a map, e.g.

```groovy
apply from: '../config/publish/android.gradle'
afterEvaluate {
    publishing {
      publications {
        myJavaLibraryName(MavenPublication, androidArtifact(
          from:                     compontents.groovy, // different components source
          artifacts:                [someJarTask], // Additional artifacts
          groupId:                  'different-group',
          artifactId:               'different-artifact-id',
          name:                     'different-name',
          version:                  'different-version',
          url:                      'different url',
          description:              'different description',
          licenseName:              'different license',
          licenseUrl:               'https://www.example.com',
          scmUrl:                   'https://www.example.com/repo.git',
          developerName:            'your name',
          developerEmail:           'your.email@example.com',
          developerOrganization:    'your org',
          developerOrganizationUrl: 'your org url',
          readme:                   'path/to/your/readme.md',
          excludeSourceJar:         false // By default, a Jar containing the module's source in added to artifacts - set to true to exlcude the Jar
        ))
      }
    }
}
```

## Maven Central Signing

If you are publishing to Maven Central, your artifacts must be signed using a PGP key. The above script has the signing config built-in, so you just need to provide the username, password, and path to your PGP private key as shown in the above examples. Also, you must distribute your public key to a key server.

See [Maven Central's Documentation](https://central.sonatype.org/pages/working-with-pgp-signatures.html) for instructions on generating and distributing a PGP key.

### Using your Signing Key on CI

Your private key MUST NOT be committed to your repository. To use it on CI, you can base 64 encode the key file, set it as an environment variable on your CI settings, and then decode the environment variable to a file at build time.

First, convert the keystore file to a base64 string:

```bash
base64 your-key.gpg
```

Copy the output of this and set it as an environment variable `RELEASE_PGP_KEY_BASE64` in your CI. Then, when running your job you can convert this base64 string back into a file.

```bash
  if [[ $RELEASE_PGP_KEY_BASE64 != "" ]]; then
      base64 -d <<< $RELEASE_PGP_KEY_BASE64 > ./your-key.gpg
  fi
```
