package ru.alfalab.gradle.platform.app

import ru.alfalab.gradle.platform.tests.base.StacksSimpleIntegrationSpec

/**
 * @author tolkv
 * @version 20/12/2017
 */
class StacksApplicationPluginIntegSpec extends StacksSimpleIntegrationSpec {
  public static final String CUSTOM_CLASSIFIER = 'myapp'

  def 'should set classifier for spring boot app'() {
    given:
      createSpringBoot1XXWithCustomClassifier('app0')
      createLibSubproject('lib0')
    when:
      def successfully = runTasksSuccessfully('build')

    then:
      successfully.wasExecuted(':app0:build')
      successfully.wasExecuted(':lib0:build')
      file('app0/build/libs').listFiles().find { it.name.endsWith('-app.jar') }
      file('lib0/build/libs').listFiles().find { not it.name.contains('app') }
  }

  def 'should set custom classifier for spring boot app'() {
    given:
      createSpringBoot1XXWithCustomClassifier('app0', CUSTOM_CLASSIFIER)
      createLibSubproject('lib0')
    when:
      def successfully = runTasksSuccessfully('build')

    then:
      file('app0/build/libs').listFiles().find { it.name.endsWith("-${CUSTOM_CLASSIFIER}.jar") }
      file('lib0/build/libs').listFiles().find { not it.name.contains(CUSTOM_CLASSIFIER) }
  }

  def 'should set classifier for spring boot 2 app'() {
    given:
      createSpringBoot2XXWithCustomClassifier('app0')
      createLibSubproject('lib0')
    when:
      def successfully = runTasksSuccessfully('build')

    then:
      successfully.wasExecuted(':app0:build')
      successfully.wasExecuted(':lib0:build')
      file('app0/build/libs').listFiles().find { it.name.endsWith('-app.jar') }
      file('lib0/build/libs').listFiles().find { not it.name.contains('app') }
  }

  def 'should set custom classifier for spring boot 2 app'() {
    given:
      createSpringBoot2XXWithCustomClassifier('app0', CUSTOM_CLASSIFIER)
      createLibSubproject('lib0')
    when:
      def successfully = runTasksSuccessfully('build')

    then:
      file('app0/build/libs').listFiles().find { it.name.endsWith("-${CUSTOM_CLASSIFIER}.jar") }
      file('lib0/build/libs').listFiles().find { not it.name.contains(CUSTOM_CLASSIFIER) }
  }
}
