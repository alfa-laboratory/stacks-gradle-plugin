package ru.alfalab.gradle.platform.stack.base

import groovy.transform.CompileStatic
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 *
 * Add plugins and configure tasks stay in good grade
 *
 * @author tolkv
 * @version 21/12/2017
 */
@CompileStatic
class StacksProjectQualityPlugin extends StacksAbstractPlugin {

  @Override
  void applyPlugin() {
    pluginManager.with {
      apply 'jacoco'
      apply 'findbugs'
      //TODO configure publish reports
    }
  }

}
