package ru.alfalab.gradle.platform.stack.base

import nebula.plugin.release.ReleasePlugin
import org.gradle.api.Task
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.tasks.TaskContainer
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
    rootProject.plugins.apply ReleasePlugin

    taskContainer.withType(ArtifactoryTask) { Task task ->
      pluginContainer.withType(JavaPlugin) {
        task.dependsOn(project.tasks.build)
      }

      rootProject.tasks.postRelease.dependsOn(task)
    }

  }
}
