package ru.alfalab.gradle.platform.stack.dependencies

import groovy.transform.CompileStatic
import io.spring.gradle.dependencymanagement.DependencyManagementPlugin
import org.gradle.api.plugins.PluginContainer
import ru.alfalab.gradle.platform.stack.api.PluginContainerAware
import ru.alfalab.gradle.platform.stack.application.StacksSpringCloudDependenciesPlugin
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
    pluginContainer.withId('org.springframework.boot') {
      //TODO wait for a fix... I don't know that to do now.
      //pluginContainer.apply StacksSpringBootDependenciesPlugin
      pluginContainer.apply StacksSpringCloudDependenciesPlugin
    }
  }

}
