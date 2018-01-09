package ru.alfalab.gradle.platform.base

import ru.alfalab.gradle.platform.stack.base.StacksReleasePlugin
import ru.alfalab.gradle.platform.stack.base.publish.StacksArtifactoryPlugin
import ru.alfalab.gradle.platform.tests.base.StacksGitIntegrationSpec

/**
 * @author tolkv
 * @version 29/12/2017
 */
class StacksReleasePluginIntegSpec extends StacksGitIntegrationSpec {

  def 'should bind post release and artifactory publish tasks'() {
    expect:
      def result = runTasksSuccessfully('snapshot')

      println result.standardOutput
      println result.standardError
      result.wasExecuted('artifactoryPublish')
  }

  @Override
  void setupProject() {
    buildFile << """\
        ${applyPlugin(StacksArtifactoryPlugin)}
        ${applyPlugin(StacksReleasePlugin)}
        apply plugin: 'java'
        group = 'ru.alfalab.platform.stacks.test'

        artifactoryPublish.skip=true
        """.stripIndent()

    git.add(patterns: ['build.gradle', '.gitignore'] as Set)
  }
}
