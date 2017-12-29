package ru.alfalab.gradle.platform.stack.base

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.invocation.Gradle
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.PluginContainer
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner
import ru.alfalab.gradle.platform.stack.base.StacksBasePlugin
import spock.lang.Specification
import spock.lang.Subject

/**
 * @author tolkv
 * @version 02/09/2017
 */
class StacksBasePluginUnitTest extends Specification {
  def project = Mock(Project)
  def gradle  = Mock(Gradle)
  def plugin  = new StacksBasePlugin() //subject

  def setup() {
    project.gradle >> gradle
    project.plugins >> Mock(PluginContainer)
    project.extensions >> Mock(ExtensionContainer)
  }

  def 'should abort build if gradle version < 4'() {
    when: 'apply plugin with incompatible gradle version'
      gradle.gradleVersion >> targetVersion
      plugin.apply(project)

    then: 'send error with message and command for obtain new gradle version'
      def e = thrown(GradleException)
      e.message.contains 'wrapper --gradle-version='

    where:
      targetVersion << ['2.1', '3.1', '3.5', '3.9']
  }
}
