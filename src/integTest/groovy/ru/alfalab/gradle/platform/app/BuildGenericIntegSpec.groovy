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
        buildscript {
          repositories {
            maven {
              url "https://plugins.gradle.org/m2/"
            }
          }
          dependencies {
            classpath "org.springframework.boot:spring-boot-gradle-plugin:1.5.10.RELEASE"
          }
        }
        ${applyPlugin(StacksBasePlugin)}
        group = 'ru.stacks.test.base'
        
        allprojects { repositories { jcenter() } }
        """.stripIndent()

    expect:
      runTasksSuccessfully('build')
  }

  def 'should configure multiproject build with spring boot 2'() {
    buildFile << """\
        buildscript {
          repositories {
            maven {
              url "https://plugins.gradle.org/m2/"
            }
          }
          dependencies {
            classpath "org.springframework.boot:spring-boot-gradle-plugin:2.0.2.RELEASE"
          }
        }

        ${applyPlugin(StacksBasePlugin)}
        group = 'ru.stacks.test.base'
        
        allprojects { repositories { jcenter() } }
        """.stripIndent()

    expect:
      runTasksSuccessfully('build')
  }

}
