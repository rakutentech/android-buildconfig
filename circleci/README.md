# CircleCI Jobs

These are [CircleCI Orbs](https://circleci.com/docs/2.0/orb-intro/) which can be used for building and publishing Android SDK projects on CircleCI. These Orbs are published to the [CircleCI Orb Registry](https://circleci.com/orbs/registry/), so you do not need to use the config files in this repo (they are here only as the source for the published Orbs).

We have two different Orbs which are published. See the each Orb's Readme for instructions on how to use:
- [android-sdk](android-sdk/README.md): Used to build and publish Android SDK projects
- [app-center](app-center/README.md): Used to publish a binary to App center. Can be used with any type of project supported by App Center, i.e. Android, iOS, etc.

## How to publish Orbs

These orbs are already published to the `rakutentech` organization, so these instructions are only relevant to users who wish to publish Orbs by themselves.

See [CircleCI docs](https://github.com/CircleCI-Public/config-preview-sdk/blob/v2.1/docs/orbs-authoring.md) for more info. In order to publish the Orbs, you must have installed the [CircleCI CLI](https://circleci.com/docs/2.0/local-cli/). On a Mac, you can install it with `brew install circleci`.

Also, you must have a Github account which belongs to the [rakutentech organization](https://github.com/rakutentech) and a CircleCI token created [here](https://circleci.com/account/api).

First, validate the orb:

```bash
circleci orb validate ./android-sdk/config.yml --token TOKEN
```

Next, if validation passed then you can publish the orb as a development version for testing:

```bash
circleci orb publish ./android-sdk/config.yml rakutentech/android-sdk@dev:0.1.0 --token TOKEN
```

Finally, you can promote the development orb to a production orb:

```bash
circleci orb publish promote rakutentech/android-sdk@dev:0.1.0 minor --token TOKEN
```

## Resources
- [Orbs Intro](https://circleci.com/docs/2.0/orb-intro/)
- [Orbs Docs](https://github.com/CircleCI-Public/config-preview-sdk/blob/v2.1/docs/README.md)
- [Orbs Registry](https://circleci.com/orbs/registry/)
