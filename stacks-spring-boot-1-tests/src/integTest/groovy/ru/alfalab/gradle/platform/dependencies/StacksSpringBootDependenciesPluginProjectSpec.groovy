package ru.alfalab.gradle.platform.dependencies

import nebula.test.IntegrationSpec
import nebula.test.PluginProjectSpec
import nebula.test.ProjectSpec
import nebula.test.dependencies.DependencyGraphBuilder
import nebula.test.dependencies.GradleDependencyGenerator
import nebula.test.dependencies.ModuleBuilder
import ru.alfalab.gradle.platform.stack.application.StacksSpringBootDependenciesPlugin
import spock.lang.Ignore

/**
 * @author tolkv
 * @version 06/06/2017
 */
class StacksSpringBootDependenciesPluginProjectSpec extends PluginProjectSpec {

  def 'should use only for Spring Boot 1XX'() {
    given:
      project.apply plugin: 'org.springframework.boot'
      project.apply plugin: getPluginName()

    expect:
      project.tasks.findByName("bootRepackage") != null
  }

  def 'should add common spring boot dependencies'() {
    when:
      project.apply plugin: 'org.springframework.boot'
      project.apply plugin: getPluginName()

    then:
      project.configurations.findAll { configuration ->
        configuration.dependencies.find {
          dep -> dep.group == 'org.springframework.boot' && dep.name == 'spring-boot-autoconfigure'
        }
      }
  }

  @Override
  String getPluginName() {
    return 'stacks.spring-boot.dependencies'
  }
}
