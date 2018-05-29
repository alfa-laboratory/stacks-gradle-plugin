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

  def 'should add dependency to default configuration'() {
    given:
      project.apply plugin: 'org.springframework.boot'
      project.apply plugin: getPluginName()
      def implementationConfiguration = project.configurations.maybeCreate('implementation')

    expect:
      implementationConfiguration.incoming.dependencies.find { it.name == 'spring-boot-autoconfigure' }

  }

  @Unroll
  def 'should return #expected for gradle version #gradleVersion'() {
    given:
      def projectMock = Mock(Project)
      def loggerMock = Mock(Logger)
      def confContainerMock = Mock(ConfigurationContainer)
      def plugin = new StacksSpringBootDependenciesPlugin()

      plugin.updateLogger loggerMock
      plugin.updateProject projectMock

      1 * projectMock.configurations >> confContainerMock
      1 * confContainerMock.findByName(_) >> realValue

    expect:
      plugin.resolveDefaultConfiguration() == expected

    where:
      expected         | realValue           | gradleVersion
      'compile'        | null                | 'before 4.7'
      'implementation' | Mock(Configuration) | 'after 4.7'
  }

  @Override
  String getPluginName() {
    return 'stacks.spring-boot.dependencies'
  }
}
