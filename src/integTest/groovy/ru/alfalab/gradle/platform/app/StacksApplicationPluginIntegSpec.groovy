package ru.alfalab.gradle.platform.app

import ru.alfalab.gradle.platform.stack.base.StacksBasePlugin
import ru.alfalab.gradle.platform.tests.base.StacksSimpleIntegrationSpec

/**
 * @author tolkv
 * @version 20/12/2017
 */
class StacksApplicationPluginIntegSpec extends StacksSimpleIntegrationSpec {
  public static final String CUSTOM_CLASSIFIER = 'myapp'

  def 'should set classifier for spring boot app'() {
    given:
      createAppSubproject('app0')
      createLibSubproject('lib0')
    when:
      def successfully = runTasksSuccessfully('build')

    then:
      file('app0/build/libs').listFiles().find { it.name.endsWith('-app.jar') }
      file('lib0/build/libs').listFiles().find { not it.name.contains('app') }
  }

  def 'should set custom classifier for spring boot app'() {
    given:
      createAppSubprojectWithCustomClassifier('app0', CUSTOM_CLASSIFIER)
      createLibSubproject('lib0')
    when:
      def successfully = runTasksSuccessfully('build')

    then:
      file('app0/build/libs').listFiles().find { it.name.endsWith("-${CUSTOM_CLASSIFIER}.jar") }
      file('lib0/build/libs').listFiles().find { not it.name.contains(CUSTOM_CLASSIFIER) }
  }
}
