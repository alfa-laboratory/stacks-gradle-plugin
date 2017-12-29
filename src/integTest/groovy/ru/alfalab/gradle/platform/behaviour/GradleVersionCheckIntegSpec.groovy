package ru.alfalab.gradle.platform.behaviour

import nebula.test.IntegrationSpec
import nebula.test.functional.ExecutionResult
import org.gradle.api.GradleException
import ru.alfalab.gradle.platform.stack.application.StacksApplicationPlugin
import ru.alfalab.gradle.platform.stack.base.StacksBasePlugin
import ru.alfalab.gradle.platform.tests.base.StacksSimpleIntegrationSpec

/**
 * @author tolkv
 * @version 15/06/2017
 */
class GradleVersionCheckIntegSpec extends StacksSimpleIntegrationSpec {

  def setup() {
    generateSevenProjectsTestGroup0()
  }

  def 'should fail with old gradle version [less than 4]'() {
    given:
      gradleVersion = '3.1'
      buildFile << """\
        ${applyPlugin(StacksApplicationPlugin)}
        group = 'ru.alfalab.platform.simple'
        
        """.stripIndent()

    when:
      def failure = runTasksWithFailure('build')

    then:
      failure.standardError.contains('Gradle will be 4+ version. For upgrade wrapper use ')
  }

}
