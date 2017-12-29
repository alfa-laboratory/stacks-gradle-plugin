package ru.alfalab.gradle.platform.stack.libraries

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.GroovyPlugin
import ru.alfalab.gradle.platform.stack.base.StacksAbstractPlugin
import ru.alfalab.gradle.platform.stack.base.StacksProgramLanguagePlugin

/**
 * Configure library project
 *
 * @author tolkv
 * @version 20/12/2017
 */
class StacksLibrariesPlugin extends StacksAbstractPlugin {

  @Override
  void applyPlugin() {
    pluginManager.apply StacksProgramLanguagePlugin
  }
}
