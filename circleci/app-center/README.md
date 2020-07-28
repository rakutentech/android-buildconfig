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
```

## Versions

### 0.1.0 (2020-07-28)

- Initial release.
