package ru.alfalab.gradle.platform.stack.base

import groovy.transform.CompileStatic
import org.gradle.api.tasks.compile.GroovyCompile
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.api.tasks.testing.Test

/**
 * @author tolkv
 * @version 23/01/2018
 */
@CompileStatic
class StacksGenericProjectPlugin extends StacksAbstractPlugin {
  @Override
  void applyPlugin() {
    // Publishing
    project.plugins.apply StacksPublicationPlugin
    project.plugins.apply 'nebula.javadoc-jar'
    project.plugins.apply 'nebula.source-jar'

    project.plugins.apply 'nebula.optional-base'
    project.plugins.apply 'nebula.integtest'

    // Info
    project.plugins.apply StacksProjectInformationPlugin

    // Contacts
    project.plugins.apply 'nebula.contacts'

    // Dependency Locking
    project.plugins.apply 'nebula.dependency-lock'
    project.plugins.apply 'nebula.dependency-recommender'

    // TODO Publish javadoc somehow
    project.tasks.withType(Javadoc) { Javadoc task ->
      task.with {
        failOnError = false
        options.encoding 'UTF-8'
      }
    }


    project.tasks.withType(GroovyCompile) { GroovyCompile t ->
      t.with {
        options.encoding = 'UTF-8'
      }
    }

    project.tasks.withType(JavaCompile) { JavaCompile t ->
      t.with {
        options.encoding = 'UTF-8'
        //For map struct and lombok
        if (!options.compilerArgs.contains('-Amapstruct.defaultComponentModel=spring')) {
          options.compilerArgs += '-Amapstruct.defaultComponentModel=spring'
        }
        if (!options.compilerArgs.contains('-Amapstruct.unmappedTargetPolicy=IGNORE')) {
          options.compilerArgs += '-Amapstruct.unmappedTargetPolicy=IGNORE'
        }
      }
    }

    project.tasks.withType(Test) { Test testTask ->
      testTask.testLogging.exceptionFormat = 'full'
    }

  }
}
