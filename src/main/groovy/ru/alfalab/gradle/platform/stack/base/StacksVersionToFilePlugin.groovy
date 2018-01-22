package ru.alfalab.gradle.platform.stack.base

import groovy.transform.CompileStatic
import org.gradle.BuildAdapter
import org.gradle.BuildResult
import org.gradle.api.Project
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
    def createProjectVersionFileTask = rootProject.tasks.maybeCreate('stacksProjectVersionFileCreateTask', StacksVersionToFileTask)

    //Add listener to write to file in any case
    gradle.addBuildListener(new BuildAdapter() {
      @Override
      void buildFinished(BuildResult result) {
        def projectVersionFile = new File(project.buildDir, 'project-version')
        if (!projectVersionFile.exists()) {
          projectVersionFile.getParentFile()?.mkdirs()
          projectVersionFile.createNewFile()
        }
        projectVersionFile.text = result.gradle.rootProject.version.toString()
      }
    })

    //support old style task, can be call directly
    allprojects { Project p ->
      p.tasks.findByName('build')?.dependsOn(createProjectVersionFileTask)
    }

  }

}