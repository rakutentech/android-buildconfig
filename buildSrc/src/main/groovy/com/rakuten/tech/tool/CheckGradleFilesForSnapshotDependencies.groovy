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

        println "Checking gradle build files for "

        def visitGradleFile = {
            print "checking $it... "
            def script = new GradleBuildScript(it)
            if(script.compileDependencies) {
                def snapshots = findAllSnapshots(script.compileDependencies)
                logger.debug("Found compile dependencies: $script.compileDependencies with snapshots: $snapshots")
                if(snapshots) {
                    println "✗"
                    throw new GradleException(
                            "Found SNAPSHOT in compile dependencies in build script $it on $snapshots")
                }
            }

            if(script.buildScriptDependencies) {
                def snapshots = findAllSnapshots(script.buildScriptDependencies)
                logger.debug("Found buildscript dependencies: $script.buildScriptDependencies with snapshots: $snapshots")
                if(snapshots) {
                    println "✗"
                    throw new GradleException(
                            "Found SNAPSHOT in buildscript dependencies in build script $it on $snapshots")
                }
            }
            println "✓"
        }

        def filter = { false }
        def isGradleFile = { it ==~ /.*\.gradle$/ }

        if (exclude instanceof Pattern) {
            filter = exclude
        } else if ( exclude && [Collection, Object[]].any { it.isAssignableFrom(exclude.getClass())
        }) {
            filter = { file ->
                if(!isGradleFile(file.absolutePath)) return true
                def shouldExclude = exclude.any {
                    file.absolutePath ==~ it
                }
                if(shouldExclude) println "excluding file $file.absolutePath"
                shouldExclude
            }
        }

        project.projectDir.traverse type: FileType.FILES,
                visit: visitGradleFile,
                nameFilter: isGradleFile,
                excludeFilter: filter

        println "No SNAPSHOTS found ✓"
    }

}

