package ru.alfalab.gradle.platform.stack.documentation.asciidoctor

import groovy.transform.CompileStatic
import org.gradle.api.plugins.PluginContainer
import ru.alfalab.gradle.platform.stack.api.PluginContainerAware
import ru.alfalab.gradle.platform.stack.base.StacksAbstractPlugin

/**
 * @author tolkv
 * @version 27/04/2018
 */
@CompileStatic
class StacksAsciidoctorStaticPlugin extends StacksAbstractPlugin implements PluginContainerAware {
  PluginContainer pluginContainer

  @Override
  void applyPlugin() {
    pluginContainer.with {
      apply 'org.asciidoctor.convert'
      apply StacksAsciidoctorDynamicPlugin
      apply StacksAsciidoctorPublishPlugin
    }
  }
}
