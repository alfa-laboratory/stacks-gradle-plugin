package ru.alfalab.gradle.platform.behaviour

import ru.alfalab.gradle.platform.stack.application.StacksApplicationPlugin
import ru.alfalab.gradle.platform.stack.base.StacksReleasePlugin
import ru.alfalab.gradle.platform.stack.base.publish.StacksArtifactoryPlugin
import ru.alfalab.gradle.platform.tests.base.StacksGitIntegrationSpec
import ru.alfalab.gradle.platform.tests.base.StacksSimpleIntegrationSpec

/**
 * @author tolkv
 * @version 15/06/2017
 */
class GradleVersionCheckGitIntegSpec extends StacksGitIntegrationSpec {

  @Override
  void setupProject() {
    buildFile << """\
        apply plugin: 'idea'
        apply plugin: 'base'
        apply plugin: 'nebula.maven-publish'
        ${applyPlugin(StacksReleasePlugin)}
        
        group = 'ru.alfalab.platform.stacks.test'

        allprojects { ${applyPlugin(StacksReleasePlugin)} }

        """.stripIndent()

    addSubproject('sub0', 'task hello')
    addSubproject('sub1', 'task hello')

    git.add(patterns: [
        'build.gradle',
        '.gitignore',
        'sub0/build.gradle',
        'sub1/build.gradle',
    ] as Set)
  }

  def 'should execute task for create project-version file in root project when multiproject build was configured'() {
    when:
      def result = runTasksSuccessfully('snapshot')

    then:
      file('build/project-version').text == '0.1.0-SNAPSHOT'
  }

}
