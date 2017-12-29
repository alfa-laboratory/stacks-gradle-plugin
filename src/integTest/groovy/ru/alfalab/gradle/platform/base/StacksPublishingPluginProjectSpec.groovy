package ru.alfalab.gradle.platform.base

import nebula.test.ProjectSpec
import ru.alfalab.gradle.platform.stack.base.StacksBasePlugin
import ru.alfalab.gradle.platform.stack.base.StacksConvention
import ru.alfalab.gradle.platform.stack.base.StacksPublicationPlugin

/**
 * @author tolkv
 * @version 26/12/2017
 */
class StacksPublishingPluginProjectSpec extends ProjectSpec {

  def 'should configure convention with injector with publication container'() {
    when:
      project.apply plugin: StacksPublicationPlugin
      def injector = project.convention.getPlugin(StacksConvention)?.stacksInjector

    then:
      assert injector
      assert injector.taskContainer
      assert injector.pluginContainer
      assert injector.extensionContainer
  }
}
