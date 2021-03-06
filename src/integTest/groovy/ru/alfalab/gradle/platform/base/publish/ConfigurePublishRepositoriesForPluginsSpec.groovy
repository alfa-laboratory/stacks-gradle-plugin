package ru.alfalab.gradle.platform.base.publish

import ru.alfalab.gradle.platform.tests.base.StacksSimpleIntegrationSpec

/**
 * @author tolkv
 * @version 02/09/2017
 */
class ConfigurePublishRepositoriesForPluginsSpec extends StacksSimpleIntegrationSpec {
  def 'should configure snapshot plugins publish repository'() {
    given:
      configureForVersion('1.0.0-SNAPSHOT')
    when:
    def result = runTasksSuccessfully('snapshot', 'showRepo')

    then:
    result.standardOutput.contains('plugins-snapshot')
  }

  def 'should configure release plugins publish repository'() {
    given:
      configureForVersion('1.0.0')
    when:
    def result = runTasksSuccessfully('final', 'showRepo')

    then:
    result.standardOutput.contains('plugins-release')
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
              usePluginsRepositories()
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
