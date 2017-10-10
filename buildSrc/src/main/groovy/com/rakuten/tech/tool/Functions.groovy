package com.rakuten.tech.tool

import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.MethodCallExpression

class Functions {
    static def findAllStatements(parent, spec) {
        def predicate = spec instanceof String ? { it == spec } : spec;
        parent.expression.arguments.expressions.get(0).code.statements.findAll {
            it.expression instanceof MethodCallExpression && predicate(it.expression.method.value)
        }
    }

    static def findAllSnapshots(List dependencies) {
        def dependencyArgs = dependencies.collect {
            it.expression.arguments.expressions.get(0) // first argument
        }
        dependencyArgs == null ? [] :
                dependencyArgs.findAll {
                    // following semver.org
                    // "A pre-release version MAY be denoted by appending a hyphen"
                    it instanceof ConstantExpression && it.value.split(':')[2].contains('-')
                }
    }
}
