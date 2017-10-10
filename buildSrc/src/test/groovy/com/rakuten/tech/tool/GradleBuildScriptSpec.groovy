package com.rakuten.tech.tool

import org.junit.Test
import static Functions.*

class GradleBuildScriptSpec {

    @Test def void shouldFindCompileDependencies() {
        def s = gradleScript("compile_dependency.gradle")
        assert s.compileDependencies.size() == 2
        assert s.buildScriptDependencies.empty
    }

    @Test def void shouldFindBuildScriptDependencies() {
        def s = gradleScript("build_script_dependency.gradle")
        assert s.compileDependencies.empty
        assert s.buildScriptDependencies.size() == 1
    }

    @Test def void shouldNotFailWithNoDependencies() {
        def s = gradleScript("no_dependency.gradle")
        assert s.compileDependencies.empty
        assert s.buildScriptDependencies.empty
    }

    @Test def void shouldFindSnapshots() {
        def s = gradleScript("compile_dependency.gradle")
        def snapshots = findAllSnapshots(s.compileDependencies)
        assert snapshots.size() == 1
    }

    static def gradleScript(name) {
        def filePath = GradleBuildScriptSpec.getClassLoader().getResource(name)
        println filePath
        println filePath.path
        def f = new File(filePath.path);
        assert f
        println f.text
        new GradleBuildScript(f)
    }
}