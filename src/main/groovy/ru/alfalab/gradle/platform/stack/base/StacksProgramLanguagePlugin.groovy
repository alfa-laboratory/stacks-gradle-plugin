package ru.alfalab.gradle.platform.stack.base

import groovy.transform.CompileStatic
import org.gradle.api.plugins.quality.CodeNarcPlugin

/**
 * @author tolkv
 * @version 21/12/2017
 */
@CompileStatic
class StacksProgramLanguagePlugin extends StacksAbstractPlugin {

  @Override
  void applyPlugin() {
    pluginManager.with {
      apply StacksProjectInformationPlugin
      apply StacksPublicationPlugin

      apply 'groovy'
      apply 'nebula.javadoc-jar'
      apply 'nebula.source-jar'
      apply 'nebula.optional-base'
      apply 'nebula.dependency-lock'
      apply 'nebula.dependency-recommender'
      apply 'nebula.integtest'

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
