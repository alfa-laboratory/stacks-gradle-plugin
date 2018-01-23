package ru.alfalab.gradle.platform.stack.base

import groovy.transform.CompileStatic
import org.gradle.api.plugins.quality.CodeNarcPlugin

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

      apply 'groovy'

      apply StacksProgramLanguageGroovydocPlugin
      apply StacksProgramLanguageGroovydocPublishingPlugin
    }

    applyCodenarcIfCodenarcXMLIsExist()
  }

  private void applyCodenarcIfCodenarcXMLIsExist() {
    if (file('config/codenarc/codenarc.xml').exists()) {
      pluginManager.apply(CodeNarcPlugin)
    }
  }

}
