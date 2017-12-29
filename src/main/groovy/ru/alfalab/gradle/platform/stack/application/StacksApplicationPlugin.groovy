package ru.alfalab.gradle.platform.stack.application

import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import ru.alfalab.gradle.platform.stack.base.StacksAbstractPlugin
import ru.alfalab.gradle.platform.stack.base.StacksProgramLanguagePlugin

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
    }
  }

}
