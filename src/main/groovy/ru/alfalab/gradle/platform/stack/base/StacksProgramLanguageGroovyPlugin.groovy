package ru.alfalab.gradle.platform.stack.base

import groovy.transform.CompileStatic
import ru.alfalab.gradle.platform.stack.dependencies.StacksDependenciesPlugin

/**
 * @author tolkv
 * @version 21/12/2017
 */
@CompileStatic
class StacksProgramLanguageGroovyPlugin extends StacksAbstractPlugin {

  @Override
  void applyPlugin() {
    pluginManager.with {
      apply StacksGenericProjectPlugin

      apply 'java'
      apply 'groovy'

      apply StacksDependenciesPlugin
      apply StacksProgramLanguageGroovydocPlugin
      apply StacksProgramLanguageGroovydocPublishingPlugin

      apply StacksCodenarcPlugin
    }

  }
}
