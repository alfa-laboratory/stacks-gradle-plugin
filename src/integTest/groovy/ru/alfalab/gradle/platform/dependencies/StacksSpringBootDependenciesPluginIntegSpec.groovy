package ru.alfalab.gradle.platform.dependencies

import nebula.test.IntegrationSpec
import nebula.test.dependencies.DependencyGraphBuilder
import nebula.test.dependencies.GradleDependencyGenerator
import nebula.test.dependencies.ModuleBuilder
import org.gradle.api.plugins.JavaPlugin
import ru.alfalab.gradle.platform.stack.application.StacksSpringBootDependenciesPlugin
import ru.alfalab.gradle.platform.stack.base.StacksBasePlugin

import static org.hamcrest.CoreMatchers.containsString
import static org.hamcrest.CoreMatchers.not
import static org.hamcrest.MatcherAssert.assertThat

/**
 * @author tolkv
 * @version 06/06/2017
 */
class StacksSpringBootDependenciesPluginIntegSpec extends IntegrationSpec {

  def 'should override spring-boo-dependencies version by stacks extension'() {
    given:
      buildFile << """\
        apply plugin: 'org.springframework.boot'
        ${applyPlugin(StacksSpringBootDependenciesPlugin)}

        group = 'ru.alfalab.stacks.dependencies.simple'
        allprojects {
          repositories {
              jcenter() //TODO use generated repo from GradleDependencyGenerator instead of public repo
          }
        }

        stacks {
          spring {
            bootVersion = '1.5.7.RELEASE' //<-- default value
          }
        }
        dependencies {
          compile 'org.springframework.boot:spring-boot-starter-web'
        }

        """.stripIndent()

    expect:
      def result = runTasksSuccessfully('dependencies')
      result.standardOutput.contains 'org.springframework.boot:spring-boot-starter-web: -> 1.5.7.RELEASE'
  }

  def 'should use 1.5.9.RELEASE as default spring version'() {
    given:
      buildFile << """\
        apply plugin: 'org.springframework.boot'
        ${applyPlugin(StacksSpringBootDependenciesPlugin)}

        group = 'ru.alfalab.stacks.dependencies.simple'
        allprojects {
          repositories {
              jcenter() //TODO use generated repo from GradleDependencyGenerator instead of public repo
          }
        }

        dependencies {
          compile 'org.springframework.boot:spring-boot-starter-web'
        }

        """.stripIndent()

    expect:
      def result = runTasksSuccessfully('dependencies')
      result.standardOutput.contains 'org.springframework.boot:spring-boot-starter-web: -> 1.5.9.RELEASE'
  }

  def 'should user version from ext.springBootVersion if extension version was not configured'() {
    given:
      buildFile << """\
        apply plugin: 'org.springframework.boot'
        ${applyPlugin(StacksSpringBootDependenciesPlugin)}

        group = 'ru.alfalab.stacks.dependencies.simple'
        allprojects {
          repositories {
              jcenter() //TODO use generated repo from GradleDependencyGenerator instead of public repo
          }
        }

        ext.springBootVersion = '1.5.7.RELEASE'

        dependencies {
          compile 'org.springframework.boot:spring-boot-starter-web'
        }

        """.stripIndent()

    expect:
      def result = runTasksSuccessfully('dependencies')
      result.standardOutput.contains 'org.springframework.boot:spring-boot-starter-web: -> 1.5.7.RELEASE'
  }


  //TODO add maven bom support for DependencyGraphBuilder
  GradleDependencyGenerator generateDependencies() {
    def graph = new DependencyGraphBuilder()
        .addModule('org.springframework.boot:spring-boot-starter-web:1.5.7.RELEASE')
        .addModule('org.springframework.boot:spring-boot-starter-web:1.5.9.RELEASE')
        .addModule('org.springframework.boot:spring-boot-starter-parent:1.5.9.RELEASE')
        .addModule('org.springframework.boot:spring-boot-starter-parent:1.5.7.RELEASE')
        .addModule('org.springframework.boot:spring-boot-dependencies:1.5.7.RELEASE')
        .addModule(new ModuleBuilder('org.springframework.boot:spring-boot-dependencies:1.5.7.RELEASE')
                       .addDependency('org.springframework.boot:spring-boot-starter-web:1.5.7.RELEASE')
                       .build())
        .addModule(new ModuleBuilder('org.springframework.boot:spring-boot-starter-parent:1.5.9.RELEASE')
                       .addDependency('org.springframework.boot:spring-boot-starter-web:1.5.9.RELEASE')
                       .build())
        .addModule(new ModuleBuilder('org.springframework.boot:spring-boot-starter-parent:1.5.7.RELEASE')
                       .addDependency('org.springframework.boot:spring-boot-starter-web:1.5.7.RELEASE')
                       .build())
        .build()

    def generator = new GradleDependencyGenerator(graph)
    generator.generateTestMavenRepo()
    generator
  }

}
