package ru.alfalab.gradle.platform.stack.libraries

import ru.alfalab.gradle.platform.stack.base.StacksAbstractPlugin
import ru.alfalab.gradle.platform.stack.base.StacksProgramLanguageGroovyPlugin

/**
 * Configure library project
 *
 * @author tolkv
 * @version 20/12/2017
 */
class StacksLibrariesPlugin extends StacksAbstractPlugin {

  @Override
  void applyPlugin() {
    pluginManager.apply 'nebula.maven-publish'
    pluginManager.apply StacksProgramLanguageGroovyPlugin
  }
}
