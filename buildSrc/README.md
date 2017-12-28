# BuildSrc
Include `config/buildSrc/build.gradle` in your projects `buildSrc/build.gradle`

```groovy
apply from: '../config/buildSrc/build.gradle'
```

### What's provided

* `CheckGradleFilesForSnapshotDependencies` task that will parse all `*.gradle` files for bulidscript classpath dependencies and project compile dependencies for pre release versions (identified by `-` in the version, following [semver.org](http://semver.org/))

### Example

```groovy
import com.rakuten.tech.tool.CheckGradleFilesForSnapshotDependencies
task preReleaseCheck(type: CheckGradleFilesForSnapshotDependencies) {
     exclude = [
             ~/.*\/config\/.*\.gradle/,
             ~/.*\/buildSrc\/.*\.gradle/,
             ~/.*\/TestUI\/.*\.gradle/,
             ]
}
```