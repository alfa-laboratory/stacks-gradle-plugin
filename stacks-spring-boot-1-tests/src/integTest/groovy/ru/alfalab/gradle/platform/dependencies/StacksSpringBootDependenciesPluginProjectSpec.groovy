package ru.alfalab.gradle.platform.dependencies

import nebula.test.PluginProjectSpec
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.logging.Logger
import ru.alfalab.gradle.platform.stack.application.StacksSpringBootDependenciesPlugin
import spock.lang.Unroll

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
