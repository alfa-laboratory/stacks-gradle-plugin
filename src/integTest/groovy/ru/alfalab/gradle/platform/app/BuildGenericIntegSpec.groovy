package ru.alfalab.gradle.platform.app

import org.gradle.api.plugins.BasePlugin
import ru.alfalab.gradle.platform.stack.base.StacksBasePlugin
import ru.alfalab.gradle.platform.tests.base.StacksSimpleIntegrationSpec

/**
 * @author tolkv
 * @version 20/12/2017
 */
class BuildGenericIntegSpec extends StacksSimpleIntegrationSpec {

  def setup() {
    generateSevenProjectsTestGroup0()
  }

  def 'should configure multiproject build'() {
    buildFile << """\
        ${applyPlugin(StacksBasePlugin)}
        group = 'ru.stacks.test.base'
        
        """.stripIndent()

    expect:
      runTasksSuccessfully('build')
  }

}
