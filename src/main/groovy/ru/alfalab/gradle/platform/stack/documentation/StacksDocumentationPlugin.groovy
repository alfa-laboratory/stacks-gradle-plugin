package ru.alfalab.gradle.platform.stack.documentation

import groovy.transform.CompileStatic
import org.gradle.api.plugins.PluginContainer
import ru.alfalab.gradle.platform.stack.api.PluginContainerAware
import ru.alfalab.gradle.platform.stack.base.StacksAbstractPlugin
import ru.alfalab.gradle.platform.stack.documentation.asciidoctor.StacksAsciidoctorDynamicPlugin
import ru.alfalab.gradle.platform.stack.documentation.asciidoctor.StacksAsciidoctorPublishPlugin

/**
 * @author tolkv
 * @version 20/12/2017
 */
@CompileStatic
class StacksDocumentationPlugin extends StacksAbstractPlugin implements PluginContainerAware {
  PluginContainer pluginContainer

  @Override
  void applyPlugin() {
    pluginContainer.withId('org.asciidoctor.convert') {
      //TODO add info to readme about configuration and purposes
      pluginContainer.apply StacksAsciidoctorDynamicPlugin

      //TODO add info to readme about publishing
      pluginContainer.apply StacksAsciidoctorPublishPlugin
    }

  }

}
