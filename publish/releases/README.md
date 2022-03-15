# Github Releases

This configuration is for create releases and release assets to [Github releases](https://docs.github.com/en/repositories/releasing-projects-on-github/managing-releases-in-a-repository).
Release assets will include the ProGard mapping files generated when building the project.

## Setup

### 1. Add your repository's environment variables

You first need to add the following environment variables to your project:

``` groovy
// the token you have generated that can be used to access the GitHub API
// https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token.
GITHUB_RELEASES_TOKEN="Access token"
// the repo owner and name eg: https://github.com/{owner}/{repo}
GITHUB_REPO_OWNER="Repository owner"
GITHUB_REPO="Repository name"
```

#### 2. Apply the plugin

Apply the plugin in your top-level `build.gradle` file.

```groovy
apply from: 'config/publish/releases/github-api.gradle'
```

### 3. Run the release gradle task

``` shell
./gradlew build githubRelease
```

This will do the following:

- Create a new Github release with the current version name. If a tag with the same name already exists, the task will fail with `Status: 422 Unprocessable Entity` server error.
- Upload the modules mappings zip file to Github Releases assets.  If a file with the same name already exists, the task will fail with `Status: 422 Unprocessable Entity` server error.
