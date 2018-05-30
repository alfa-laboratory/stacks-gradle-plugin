package ru.alfalab.gradle.platform.base

import ru.alfalab.gradle.platform.tests.base.StacksSimpleIntegrationSpec

class StacksDefaultRepositoryPluginIntegSpec extends StacksSimpleIntegrationSpec {
  def setup() {
    buildFile << """\

        apply plugin: ru.alfalab.gradle.platform.stack.base.StacksDefaultRepositoryPlugin
        apply plugin: ru.alfalab.gradle.platform.stack.base.StacksDefaultRepositoryPlugin
        apply plugin: ru.alfalab.gradle.platform.stack.base.StacksDefaultRepositoryPlugin
        apply plugin: ru.alfalab.gradle.platform.stack.base.StacksDefaultRepositoryPlugin
        
        repositories {
          maven { url 'localhost' } 
          jcenter()
        }
        
        task showRepositories { 
          doLast {
            repositories.each {
              logger.quiet 'mymark---' +it.name
            }
          }
        }
         
        """.stripIndent()
  }

  def 'should set default repo only one times'() {
    when:
      def successfully = runTasksSuccessfully('showRepositories')

    then:
      successfully.wasExecuted(':showRepositories')
      successfully.standardOutput.readLines().findAll { it.contains 'mymark---BintrayJCenter' }.size() == 1
  }
}
