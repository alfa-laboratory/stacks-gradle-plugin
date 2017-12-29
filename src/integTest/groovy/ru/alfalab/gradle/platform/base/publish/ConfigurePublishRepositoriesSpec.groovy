package ru.alfalab.gradle.platform.base.publish

import ru.alfalab.gradle.platform.tests.base.StacksSimpleIntegrationSpec

/**
 * @author tolkv
 * @version 02/09/2017
 */
class ConfigurePublishRepositoriesSpec extends StacksSimpleIntegrationSpec {
  def 'should configure snapshot publish repository'() {
    given:
      configureForVersion('1.0.0-SNAPSHOT')
    when:
      def result = runTasksSuccessfully('snapshot', 'showRepo')

    then:
      result.wasExecuted('artifactoryPublish')
      result.standardOutput.contains('mysnapshto')
  }

  def 'should configure release publish repository'() {
    given:
      configureForVersion('1.0.0')
    when:
      def result = runTasksSuccessfully('final', 'showRepo')

    then:
      result.wasExecuted('artifactoryPublish')
      result.standardOutput.contains('myrelease')
  }

  def configureForVersion(String version) {
    buildFile << """\
        apply plugin: 'stacks.artifactory'
        apply plugin: 'java'
        group = 'ru.alfalab.test'
        version = '$version'

        stacks {
          publishing {
            repositories {
              snapshot 'mysnapshto'
              release 'myrelease'
            }
          }
        }
        artifactoryPublish.skip=true //hack wich helps artifactory stay clean
        
        task showRepo {
          doLast {
            logger.quiet project.convention.findPlugin(org.jfrog.gradle.plugin.artifactory.dsl.ArtifactoryPluginConvention).publisherConfig.publisher.repoKey
          }
        }
        task snapshot { dependsOn 'artifactoryPublish' }
        task 'final'() { dependsOn 'artifactoryPublish' }
        """.stripIndent()

  }
}
