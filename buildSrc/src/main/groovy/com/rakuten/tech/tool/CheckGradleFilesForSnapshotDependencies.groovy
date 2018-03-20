package com.rakuten.tech.tool

import groovy.io.FileType
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction

import java.util.regex.Pattern

import static com.rakuten.tech.tool.Functions.findAllSnapshots

@SuppressWarnings("GroovyUnusedDeclaration")
public class CheckGradleFilesForSnapshotDependencies extends DefaultTask {
  /**
   * Regex or collection of Regexes to exclude files/paths from filtering.
   * Example configurations:
   *
   * ```groovy
   * // single
   * exclude = ~/.*\/Example\/.*\.gradle/
   *
   * // list of Regexes
   * exclude = [
   *          ~/.*\/Example\/.*\.gradle/ ,
   *          ~/.*\/Runtime\/.*\.gradle/
   *          ]
   * ```
   */
  def exclude

  CheckGradleFilesForSnapshotDependencies() {
    description = "Check build files for SNAPSHOT dependencies"
    group = 'Verification'
  }

  @TaskAction
  void findAndCheckBuildFiles() {
    logger.info("Checking gradle build files for dependencies in pre-release versions...")

    if (project.version.contains('-')) {
      logger.info("Project version $project.name is in $project.version which is pre-release, skipping snapshot checks ✓")
      return
    }

    def visitGradleFile = {
      logger.info("checking $it... ")
      def script = new GradleBuildScript(it)
      if (script.compileDependencies) {
        def snapshots = findAllSnapshots(script.compileDependencies)
        logger.info("Found compile dependencies: $script.compileDependencies with snapshots: $snapshots")
        if (snapshots) {
          logger.info("✗")
          throw new GradleException("Found SNAPSHOT in compile dependencies in build script $it on $snapshots")
        }
      }

      if (script.buildScriptDependencies) {
        def snapshots = findAllSnapshots(script.buildScriptDependencies)
        logger.info("Found buildscript dependencies: $script.buildScriptDependencies with snapshots: $snapshots")
        if (snapshots) {
          logger.info("✗")
          throw new GradleException("Found SNAPSHOT in buildscript dependencies in build script $it on $snapshots")
        }
      }
      logger.info("✓")
    }

    def filter = { false }
    def isGradleFile = { it ==~ /.*\.gradle$/ }

    if (exclude instanceof Pattern) {
      filter = exclude
    } else if (exclude && [Collection, Object[]].any {
      it.isAssignableFrom(exclude.getClass())
    }) {
      filter = { file ->
        if (!isGradleFile(file.absolutePath)) return true
        def shouldExclude = exclude.any {
          file.absolutePath ==~ it
        }
        if (shouldExclude) logger.info("excluding file $file.absolutePath")
        shouldExclude
      }
    }

    project.projectDir.traverse type: FileType.FILES,
        visit: visitGradleFile,
        nameFilter: isGradleFile,
        excludeFilter: filter

    logger.info("No SNAPSHOTS found ✓")
  }

}

