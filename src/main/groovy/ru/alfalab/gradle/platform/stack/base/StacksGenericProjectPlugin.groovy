package ru.alfalab.gradle.platform.stack.base

import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.api.tasks.testing.Test

/**
 * @author tolkv
 * @version 23/01/2018
 */
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
    project.tasks.withType(Javadoc) {
      failOnError = false
    }
    project.tasks.withType(Test) { Test testTask ->
      testTask.testLogging.exceptionFormat = 'full'
    }

  }
}
