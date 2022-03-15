# android-sdk Orb

See the [android-sdk Orb page](https://circleci.com/orbs/registry/orb/rakutentech/android-sdk) for details on the jobs included by this Orb.

## Basic Setup

This basic setup will do the following:
- Checkout the Git repo and initialize any Git submodules.
- Run the `build` job.
    - Run Gradle's `check` and `assemble` commands.
    - Upload coverage to (Code Cov)(https://codecov.io).
    - Store artifacts: `reports`, `test-results`, `aar`, and `apk`
    - Persists to workspace: the `build` folder for `sdk-path` and the `apk` folder for the `sample-app-path`.
- If this is a release (a Git tag that starts with `v`), then pause for user verification.
- If verified by user, then run the `publish` job.
    - Run Gradle's `publish` command.

```yml
version: 2.1

orbs:
  android-sdk: rakutentech/android-sdk:{CURRENT_VERSION}

workflows:
  version: 2.1
  build-and-release:
    jobs:
      - android-sdk/build:
          gradle-cache-key: gradle-{{ checksum "build.gradle" }}-{{ checksum "YOUR_SDK_PATH/build.gradle" }}
          maven-cache-key: maven-{{ checksum "SDK_PATH/src/test/AndroidManifest.xml" }}
          sdk-path: YOUR_SDK_PATH
          sample-app-path: YOUR_SAMPLE_APP_PATH
          # You can optionally define `pre-steps` which will run before any other steps in the job.
          pre-steps:
            - run:
                command: echo "Pre steps"
          # You can optionally define `after-prepare-steps` commands which will run after the project has been checked out and the Gradle dependencies have been downloaded
          after-prepare-steps:
            - run:
                command: echo "After prepare"
          # You can optionally define `post-steps` which will run after all other steps in the job have completed.
          post-steps:
            - run:
                command: echo "Post steps"
          filters:
            tags:
              only: /^v.*/
            branches:
              only: /.*/

      - release-verification:
          type: approval
          requires:
            - android-sdk/build
          filters:
            tags:
              only: /^v.*/
            branches:
              ignore: /.*/

      - android-sdk/publish:
          requires:
            - release-verification
          filters:
            tags:
              only: /^v.*/
            branches:
              ignore: /.*/
```

## Advanced

### Building Release APK using a release keystore

You may want to build and publish an APK which uses your release keystore. In that case, you don't want to commit the keystore to your repository due to security reasons.

Instead, you can convert the keystore file to a base64 string, then add that string to CircleCI as an environment variable (in the project settings).

First, convert the keystore file to a base64 string:

```bash
openssl base64 -A -in release-keystore.jks
```

Copy the output of this and set it as an environment variable `RELEASE_KEYSTORE_BASE64` in your CircleCI project settings.

Finally, when running your job you can convert this base64 string back into a file. If you are using the `android-sdk` orb, then you can add this to the pre-steps section.

```yml
- run: |
    if [[ $RELEASE_KEYSTORE_BASE64 != "" ]]; then
        base64 -d \<<< $RELEASE_KEYSTORE_BASE64 > ./release-keystore.jks
    fi
```

## Versions

### 0.3.0 (2022-01-25)

- Update docker image to `android:api-30`. This docker image uses Java 11, so your project must support building with Java 11

### 0.2.1 (2021-03-08)

- Fix `after-prepare-steps` property for the `publish` job.

### 0.2.0 (2020-08-19)

- Pin Android image to the latest version of api-29 which supported Java 8.
- Publish job: add `after-prepare-steps` parameter.

### 0.1.0 (2020-07-28)

- Initial release.
