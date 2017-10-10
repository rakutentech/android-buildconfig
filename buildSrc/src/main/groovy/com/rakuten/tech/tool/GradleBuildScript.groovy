package com.rakuten.tech.tool

import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.ErrorCollector
import org.codehaus.groovy.control.SourceUnit
import static Functions.*

class GradleBuildScript {
    SourceUnit src
    def compileDependencies
    def buildScriptDependencies

    GradleBuildScript(File sourceFile) {
        CompilerConfiguration configuration = new CompilerConfiguration();
        configuration.setTolerance(1);

        src = new SourceUnit(sourceFile, configuration, null, new ErrorCollector(configuration));
        src.parse()
        src.completePhase()
        src.convert()

        compileDependencies = compileDependencies()
        buildScriptDependencies = buildScriptDependencies();
    }

    def compileDependencies() {
        def dependenciesBlock = topLevel 'dependencies'
        dependenciesBlock ? findAllStatements(dependenciesBlock, {
            it.toLowerCase().contains('compile')
        }) : []
    }

    def buildScriptDependencies() {
        def buildScriptBlock = topLevel 'buildscript'
        def dependencies = buildScriptBlock ? findAllStatements(buildScriptBlock, "dependencies") : []
        dependencies ? findAllStatements(dependencies.get(0), 'classpath') : [];
    }

    def topLevel(blockName) {
        src.ast.statementBlock.statements.find {
                    it.hasProperty('expression') &&
                    it.expression instanceof MethodCallExpression &&
                    it.expression.method.value == blockName
        }
    }


}

