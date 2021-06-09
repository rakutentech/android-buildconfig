# app-center Orb

See the [app-center Orb page](https://circleci.com/orbs/registry/orb/rakutentech/app-center) for details on the jobs included by this Orb.

## Basic Setup

This basic setup will do the following:
- Publish the provided file to App Center.
    - You can use this job multiple times to publish different binaries, but note that you must define a unique `name` for each job.

```yml
version: 2.1

orbs:
  app-center: rakutentech/app-center:{CURRENT_VERSION}

workflows:
  version: 2.1
  build-and-release:
    jobs:
      # Run your build job
      - android-sdk/build:
          ...
          # this is convenient if you want to use the app-center/mapping job
          post-steps:
            - persist_to_workspace:
                root: yourapp/build/outputs
                paths:
                  - mapping/
      # Publish STG build to Testers
      - app-center/publish:
          name: publish-test-app-stg    
          group: Testers
          file: apk/staging/testapp-staging.apk
          app: $APP_CENTER_APP_NAME
          token: $APP_CENTER_TOKEN
          notes: $CIRCLE_BUILD_URL
          requires:
            - build
          filters:
            branches:
              only: master

      # Publish production build to a different group
      - app-center/publish:
          name: publish-test-app-prod
          group: Production
          file: apk/release/testapp-release.apk
          app: $APP_CENTER_APP_NAME
          token: $APP_CENTER_TOKEN
          notes: Production build for $CIRCLE_TAG
          requires:
            - build
          filters:
            tags:
              only: /^v.*/
            branches:
              ignore: /.*/
      # Upload mapping for production build
      - app-center/mapping:
          name: publish-mapping-for-test-app-prod
          file: mapping/release/mapping.txt
          app: $APP_CENTER_APP_NAME
          token: $APP_CENTER_TOKEN
          version-name: $APP_CENTER_VERSION_NAME
          version-code: $APP_CENTER_VERSION_CODE
          requires:
            - build
          filters:
            tags:
              only: /^v.*/
            branches:
              ignore: /.*/
```

## Versions

### 0.1.3 (2021-06-09)

- **Feature:** added mapping job to upload de-symbolization/de-obfuscation mapping files

### 0.1.2 (2020-10-05)

- **Fixed:** `notes` parameter didn't allow environment variables to be used.

### 0.1.1 (2020-09-03)

- **Fixed:** Publishing was failing when the `notes` parameter contained spaces.

### 0.1.0 (2020-07-28)

- Initial release.
