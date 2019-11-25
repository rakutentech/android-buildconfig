# Android Documention Theme for Github Pages

This is a Jekyll theme for GitHub Pages. It's intended to allow you to have published documentation for multiple versions of your a library. The viewer of the docs wiill be able to select which version they wish to view.

## How to use

### 1. Enable GitHub Pages

Create a new branch on Github called `gh-pages`, and then enable [Github Pages](https://help.github.com/en/enterprise/2.15/user/articles/configuring-a-publishing-source-for-github-pages) for your repo.

### 2. Add config

In your `gh-pages` branch, create a file named `_config.yml` with the following content:

```yml
plugins:
  - github-pages

remote_theme: rakutentech/android-sdkutils@gh-pages-theme

title: Your repo title
description: Description of your repo
repository: your-org-name/your-repo-name

#Optional:
docs_path: docs # Path where docs will be stored
api_docs_path: api # Path (within `docs` path) where API docs are stored

collections: 
  versions: # Folder name where versions are stored
```

### 3. Add versions

Create a folder named `_versions`. This folder should contain files representing your published versions. For example, for version `1.0`, you should have a file named `1.0.md` with the following contents:

```
---
version: 1.0
---
```

### 4. Add docs for versions

Create a folder named `docs` which will contain the published documentation. The folder structure should look something like this:

```
docs 
│
├── 1.0
│   ├── index.md  (Should have the 'userguide' layout applied)
│   │
│   └───api
│       └── index.md
│
└── 2.0
    │   index.md  (Should have the 'userguide' layout applied)
    │
    └── api
        └── index.md
```

Note that the files under `docs/VERSION_NAME/index.md` should have the `userguide` layout applied. This can be done by adding the following to the top of the markdown file.

```
---
layout: userguide
---

# The rest of your markdown content is here
```

### 5. Test locally

You can test locally by using Ruby's `bundle` command. First, you will need to create a file in the root of your pages site called `Gemfile`:

```
source "https://rubygems.org"

gem "github-pages", "197", group: :jekyll_plugins
```

Next, run the following commands:

1. `bundle install`
2. `bundle exec jekyll serve`
