package ru.alfalab.gradle.platform.ci

import nebula.test.IntegrationSpec
import ru.alfalab.gradle.platform.stack.application.StacksSpringBootDependenciesPlugin
import ru.alfalab.gradle.platform.stack.base.StacksBasePlugin
import ru.alfalab.gradle.platform.stack.base.StacksVersionToFilePlugin

import static org.hamcrest.CoreMatchers.containsString
import static org.hamcrest.CoreMatchers.not
import static org.hamcrest.MatcherAssert.assertThat

/**
 * @author tolkv
 * @version 06/06/2017
 */
class StacksVersionToFilePluginIntegSpec extends IntegrationSpec {

  def 'should override spring-boo-dependencies version by stacks extension'() {
    given:
      buildFile << """\
        ${applyPlugin(StacksVersionToFilePlugin)}
        group = 'ru.alfalab.project.version'
        version = '1.0.0'
        """.stripIndent()

    expect:
      def result = runTasksSuccessfully('build')
      result.wasExecuted('build')

      file('build/project-version').text == '1.0.0'
  }

}
