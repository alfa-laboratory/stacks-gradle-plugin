package ru.alfalab.gradle.platform.stack.base

import groovy.transform.CompileStatic
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.tasks.TaskContainer
import org.gradle.language.base.plugins.LifecycleBasePlugin
import ru.alfalab.gradle.platform.stack.api.PluginContainerAware
import ru.alfalab.gradle.platform.stack.api.TaskContainerAware

/**
 * @author tolkv
 * @version 15/01/2018
 */
@CompileStatic
class StacksVersionToFilePlugin extends StacksAbstractPlugin implements TaskContainerAware, PluginContainerAware {
  PluginContainer pluginContainer
  TaskContainer   taskContainer

  @Override
  void applyPlugin() {
    rootProject.with { root ->
      def createProjectVersionFileTask = root.tasks.maybeCreate('stacksProjectVersionFileCreateTask', StacksVersionToFileTask)
      plugins.withType(LifecycleBasePlugin) {
        root.tasks.findByName('build').dependsOn(createProjectVersionFileTask)
      }
    }

  }
}
