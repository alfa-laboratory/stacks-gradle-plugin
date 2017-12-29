package ru.alfalab.gradle.platform.base.publish

import ru.alfalab.gradle.platform.tests.base.StacksSimpleIntegrationSpec

/**
 * @author tolkv
 * @version 02/09/2017
 */
class ConfigurePublishRepositoriesForLibsSpec extends StacksSimpleIntegrationSpec {
  def 'should configure snapshot libs publish repository'() {
    given:
      configureForVersion('1.0.0-SNAPSHOT')
    when:
    def result = runTasksSuccessfully('snapshot', 'showRepo')

    then:
    result.standardOutput.contains('libs-snapshot')
  }

  def 'should configure release libs publish repository'() {
    given:
      configureForVersion('1.0.0')
    when:
    def result = runTasksSuccessfully('final', 'showRepo')

    then:
    result.standardOutput.contains('libs-release')
  }

  def configureForVersion(String version) {
    buildFile << """\
        apply plugin: 'java'
        apply plugin: 'stacks.artifactory'
        
        group = 'ru.alfalab.test'
        version = '$version'

        stacks {
          publishing {
            repositories {
              useLibsRepositories()
            }
          }
        }                
        
        artifactoryPublish.skip=true

        task showRepo {
          doLast {
            logger.quiet project.convention.findPlugin(org.jfrog.gradle.plugin.artifactory.dsl.ArtifactoryPluginConvention).publisherConfig.publisher.repoKey
          }
        }
                
        task snapshot { }
        task 'final'() { } 
        """.stripIndent()

  }
}
