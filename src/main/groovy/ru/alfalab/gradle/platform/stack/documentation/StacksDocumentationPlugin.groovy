package ru.alfalab.gradle.platform.stack.documentation

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer
import ru.alfalab.gradle.platform.stack.api.PluginContainerAware
import ru.alfalab.gradle.platform.stack.base.StacksAbstractPlugin
import ru.alfalab.gradle.platform.stack.documentation.asciidoctor.StacksAsciidoctorDynamicPlugin

/**
 * @author tolkv
 * @version 20/12/2017
 */
class StacksDocumentationPlugin extends StacksAbstractPlugin implements PluginContainerAware {
  PluginContainer pluginContainer

  @Override
  void applyPlugin() {
    //TODO configure asciidoctor
    pluginContainer.withId('org.asciidoctor.convert') {
      pluginContainer.apply StacksAsciidoctorDynamicPlugin
    }

    //TODO configure publish docs and export urls with docs and meta info
  }

}
