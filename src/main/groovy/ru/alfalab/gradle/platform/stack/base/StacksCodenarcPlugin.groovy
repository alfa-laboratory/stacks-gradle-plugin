package ru.alfalab.gradle.platform.stack.base

import groovy.transform.CompileStatic
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.plugins.quality.CodeNarcPlugin

@CompileStatic
class StacksCodenarcPlugin extends StacksAbstractPlugin {
  @Override
  void applyPlugin() {
    plugins.withType(GroovyPlugin) {
      applyCodenarcIfCodenarcXMLIsExist()
    }
  }


  private void applyCodenarcIfCodenarcXMLIsExist() {
    if (file('config/codenarc/codenarc.xml').exists()) {
      pluginManager.apply(CodeNarcPlugin)
    }
  }
}
