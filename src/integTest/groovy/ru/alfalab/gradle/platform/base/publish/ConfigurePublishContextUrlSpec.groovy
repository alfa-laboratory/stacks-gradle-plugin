package ru.alfalab.gradle.platform.base.publish

import ru.alfalab.gradle.platform.tests.base.StacksSimpleIntegrationSpec

/**
 * @author tolkv
 * @version 02/09/2017
 */
class ConfigurePublishContextUrlSpec extends StacksSimpleIntegrationSpec {
  def 'should override artifactory context url'() {
    given:
      configureForVersion('1.0.0-SNAPSHOT')
    when:
      def result = runTasksSuccessfully('snapshot', 'taskForTest')

    then:
      result.wasExecuted('artifactoryPublish')
      println result.standardOutput
      result.standardOutput.contains('---http://supertesthost/')
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
        
        artifactory {
          contextUrl = 'http://supertesthost/'
        }
        
        artifactoryPublish.skip=true
        
        task taskForTest {
          doLast {
            logger.quiet '---'+project.convention.findPlugin(org.jfrog.gradle.plugin.artifactory.dsl.ArtifactoryPluginConvention).publisherConfig.publisher.contextUrl
          }
        }
        task snapshot { dependsOn 'artifactoryPublish' }
        task 'final'() { dependsOn 'artifactoryPublish' }
        """.stripIndent()
  }
}
