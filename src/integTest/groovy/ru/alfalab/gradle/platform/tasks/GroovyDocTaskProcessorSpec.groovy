package ru.alfalab.gradle.platform.tasks

import nebula.test.ProjectSpec
import org.gradle.api.tasks.javadoc.Groovydoc

/**
 * @author tolkv
 * @version 20/09/2017
 */
class GroovyDocTaskProcessorSpec extends ProjectSpec {

  def 'should configure groovydocJar tasks'() {
    when:
    project.apply plugin: 'groovy'
    project.apply plugin: 'stacks.lang.groovy'
    project.configure(project) {
      group = 'mygroup'
    }

    then:
    noExceptionThrown()

    project.tasks.getByName('groovydocJar') != null
    project.tasks.getByName('groovydocJar').dependsOn.find { it instanceof Groovydoc }

  }

}
