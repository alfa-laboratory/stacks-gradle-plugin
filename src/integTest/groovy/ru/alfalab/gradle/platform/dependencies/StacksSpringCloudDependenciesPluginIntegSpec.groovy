package ru.alfalab.gradle.platform.dependencies

import nebula.test.IntegrationSpec
import nebula.test.dependencies.DependencyGraphBuilder
import nebula.test.dependencies.GradleDependencyGenerator
import nebula.test.dependencies.ModuleBuilder
import ru.alfalab.gradle.platform.stack.application.StacksSpringCloudDependenciesPlugin

/**
 * @author tolkv
 * @version 06/06/2017
 */
class StacksSpringCloudDependenciesPluginIntegSpec extends IntegrationSpec {

  def 'should override spring-cloud-dependencies version by stacks extension'() {
    given:
      buildFile << """\
        apply plugin: 'org.springframework.boot'
        ${applyPlugin(StacksSpringCloudDependenciesPlugin)}

        group = 'ru.alfalab.stacks.dependencies.cloud.simple'
        allprojects {
          repositories {
              mavenCentral() //TODO use generated repo from GradleDependencyGenerator instead of public repo
          }
        }

        stacks {
          spring {
            cloudVersion 'Brixton.SR1' //<-- default value
          }
        }
        dependencies {
          compile 'org.springframework.cloud:spring-cloud-starter-eureka'
        }

        """.stripIndent()

    expect:
      def result = runTasksSuccessfully('dependencies')
      result.standardOutput.contains 'org.springframework.cloud:spring-cloud-starter-eureka -> 1.1.2' //sign of Brixton.SR1
  }

  def 'should use Edgware.SR1 as default spring cloud version'() {
    given:
      buildFile << """\
        apply plugin: 'org.springframework.boot'
        ${applyPlugin(StacksSpringCloudDependenciesPlugin)}

        group = 'ru.alfalab.stacks.dependencies.cloud.simple'
        allprojects {
          repositories {
              mavenCentral() //TODO use generated repo from GradleDependencyGenerator instead of public repo
          }
        }

        dependencies {
          compile 'org.springframework.cloud:spring-cloud-starter-eureka'
        }

        """.stripIndent()

    expect:
      def result = runTasksSuccessfully('dependencies')
      result.standardOutput.contains 'org.springframework.cloud:spring-cloud-starter-eureka -> 1.4.2' //sign of Edgwarder.SR1
  }

  //TODO add maven bom support for DependencyGraphBuilder
  GradleDependencyGenerator generateDependencies() {

  }

}
