package ru.alfalab.gradle.platform.base

import nebula.test.IntegrationSpec
import ru.alfalab.gradle.platform.stack.base.StacksBasePlugin

import static org.hamcrest.CoreMatchers.containsString
import static org.hamcrest.CoreMatchers.not
import static org.hamcrest.MatcherAssert.assertThat

/**
 * @author tolkv
 * @version 06/06/2017
 */
class BasicChecksIntegSpec extends IntegrationSpec {

  def 'should fail if group does\'t exist and advice set group'() {
    given:
      buildFile << """\
        ${applyPlugin(StacksBasePlugin)}
        """.stripIndent()

    expect:
      def result = runTasksWithFailure('build')
      result.standardError.contains('group id must be set')
      result.standardError.contains('group = \'ru.alfalab')
  }

  def 'should work with group'() {
    given:
      buildFile << """\
        ${applyPlugin(StacksBasePlugin)}
        apply plugin: 'groovy'
        group = 'ru.alfalab.platform.eureka'
        
        """.stripIndent()

    expect: 'compile and does not contain spring dependencies'
      runTasksSuccessfully 'compileGroovy'
      def dependenciesOutput = runTasksSuccessfully('dependencies').standardOutput
      assertThat 'should not add spring dependencies', dependenciesOutput, not(containsString('.spring'))
  }

}
