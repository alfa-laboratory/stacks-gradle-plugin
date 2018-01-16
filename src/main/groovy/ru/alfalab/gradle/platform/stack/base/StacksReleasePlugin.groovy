package ru.alfalab.gradle.platform.stack.base

import nebula.plugin.release.ReleasePlugin
import org.gradle.api.Task
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.tasks.TaskContainer
import org.gradle.language.base.plugins.LifecycleBasePlugin
import org.jfrog.gradle.plugin.artifactory.task.ArtifactoryTask
import ru.alfalab.gradle.platform.stack.api.PluginContainerAware
import ru.alfalab.gradle.platform.stack.api.TaskContainerAware

/**
 * @author tolkv
 * @version 29/12/2017
 */
class StacksReleasePlugin extends StacksAbstractPlugin implements TaskContainerAware, PluginContainerAware {
  TaskContainer   taskContainer
  PluginContainer pluginContainer

  @Override
  void applyPlugin() {
    if(rootProject == project) {
      rootProject.plugins.apply ReleasePlugin
      rootProject.plugins.apply StacksVersionToFilePlugin
    } else {
      warn 'stacks.release plugin must be applied only to root project'
      rootProject.pluginManager.apply StacksReleasePlugin
    }
  }
}
