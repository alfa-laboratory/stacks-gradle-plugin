package ru.alfalab.gradle.platform.base

import nebula.test.PluginProjectSpec
import org.gradle.api.plugins.JavaPlugin
import ru.alfalab.gradle.platform.stack.application.StacksApplicationPlugin
import ru.alfalab.gradle.platform.stack.base.StacksBasePlugin
import ru.alfalab.gradle.platform.stack.base.StacksReleasePlugin
import ru.alfalab.gradle.platform.stack.base.publish.StacksArtifactoryPlugin
import ru.alfalab.gradle.platform.tests.base.StacksSimpleIntegrationSpec

/**
 * @author tolkv
 * @version 29/12/2017
 */
class StacksReleasePluginProjectSpec extends StacksSimpleIntegrationSpec {
  def 'should warn when apply release plugin to subproject'() {
    given:
      createSubprojectWithPlugin('app0', StacksReleasePlugin)
      createSubprojectWithPlugin('app1', StacksReleasePlugin)

      buildFile << """\
        ${applyPlugin(StacksBasePlugin)}
        group = 'ru.alfalab.platform.release.simple'
   
        task checkRelease() {
          dependsOn build
          doLast {
            logger.quiet 'should apply stacks.release plugin to rootProject='+rootProject.pluginManager.hasPlugin('stacks.release')
          }
        }     
        """.stripIndent()

    when:
      def runResult = runTasksSuccessfully('checkRelease')

    then:
      runResult.standardOutput.contains('stacks.release plugin must be applied only to root project')
      runResult.standardOutput.contains('should apply stacks.release plugin to rootProject=true')
  }

  def 'should not warn if apply release plugin only to root project'() {
    given:
      createSubprojectWithPlugin('app0', StacksBasePlugin)
      createSubprojectWithPlugin('app1', StacksBasePlugin)

      buildFile << """\
        ${applyPlugin(StacksReleasePlugin)}
        group = 'ru.alfalab.platform.release.simple'
        
        """.stripIndent()

    when:
      def runResult = runTasksSuccessfully('build')

    then:
      !runResult.standardOutput.contains('stacks.release plugin must be applied only to root project')
  }

}
