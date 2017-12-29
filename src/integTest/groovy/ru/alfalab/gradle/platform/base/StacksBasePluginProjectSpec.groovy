package ru.alfalab.gradle.platform.base

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import nebula.test.ProjectSpec
import org.gradle.api.plugins.BasePlugin
import ru.alfalab.gradle.platform.stack.StacksContainerInjector
import ru.alfalab.gradle.platform.stack.base.StacksBasePlugin
import ru.alfalab.gradle.platform.stack.base.StacksConvention
import ru.alfalab.gradle.platform.stack.base.StacksDefaultRepositoryPlugin
import ru.alfalab.gradle.platform.tests.base.StacksSimpleIntegrationSpec

/**
 * @author tolkv
 * @version 26/12/2017
 */
class StacksBasePluginProjectSpec extends ProjectSpec {

  def 'should configure convention with injector'() {
    when:
      project.apply plugin: StacksBasePlugin
      def injector = project.convention.getPlugin(StacksConvention)?.stacksInjector

    then:
      injector
      injector.taskContainer
      injector.pluginContainer
      injector.extensionContainer

      project.plugins.hasPlugin 'nebula.info'
      project.plugins.hasPlugin 'nebula.contacts'
      project.plugins.hasPlugin BasePlugin
      project.plugins.hasPlugin StacksDefaultRepositoryPlugin
  }

}
