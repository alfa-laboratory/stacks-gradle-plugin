package ru.alfalab.gradle.platform.stack.dependencies

import groovy.transform.CompileStatic
import io.spring.gradle.dependencymanagement.DependencyManagementPlugin
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.tasks.TaskContainer
import ru.alfalab.gradle.platform.stack.api.ExtensionContainerAware
import ru.alfalab.gradle.platform.stack.api.PluginContainerAware
import ru.alfalab.gradle.platform.stack.api.TaskContainerAware
import ru.alfalab.gradle.platform.stack.application.StacksSpringBootDependenciesPlugin
import ru.alfalab.gradle.platform.stack.base.StacksAbstractPlugin

/**
 * @author tolkv
 * @version 20/12/2017
 */
@CompileStatic
class StacksDependenciesPlugin extends StacksAbstractPlugin implements PluginContainerAware {
  PluginContainer    pluginContainer

  @Override
  void applyPlugin() {
    pluginContainer.apply DependencyManagementPlugin
    if (pluginContainer.hasPlugin('org.springframework.boot')) {
      pluginContainer.apply StacksSpringBootDependenciesPlugin
    }
  }

}
