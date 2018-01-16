package ru.alfalab.gradle.platform.stack.application

import groovy.transform.CompileStatic
import ru.alfalab.gradle.platform.stack.base.StacksAbstractPlugin
import ru.alfalab.gradle.platform.stack.base.StacksProgramLanguagePlugin
import ru.alfalab.gradle.platform.stack.dependencies.StacksDependenciesPlugin

/**
 * @author tolkv
 * @version 20/12/2017
 */
@CompileStatic
class StacksApplicationPlugin extends StacksAbstractPlugin {

  @Override
  void applyPlugin() {
    pluginManager.with {
      apply StacksProgramLanguagePlugin
      apply 'org.springframework.boot'
      apply StacksApplicationSpringBootTaskPlugin
      apply StacksApplicationSpringBootPublishingPlugin
      apply StacksDependenciesPlugin
    }
  }

}
