package ru.alfalab.gradle.platform.stack.base

import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.javadoc.Groovydoc
import org.gradle.jvm.tasks.Jar
import org.jfrog.gradle.plugin.artifactory.ArtifactoryPlugin
import org.jfrog.gradle.plugin.artifactory.task.ArtifactoryTask
import ru.alfalab.gradle.platform.stack.api.ExtensionContainerAware
import ru.alfalab.gradle.platform.stack.api.PluginContainerAware
import ru.alfalab.gradle.platform.stack.api.TaskContainerAware

/**
 * @author tolkv
 * @version 21/12/2017
 */
@CompileStatic
class StacksProgramLanguageGroovydocPlugin extends StacksAbstractPlugin implements PluginContainerAware, TaskContainerAware, ExtensionContainerAware{
  public static final String             GROOVYDOC_JAR_TASK = 'groovydocJar'
                      ExtensionContainer extensionContainer
                      PluginContainer    pluginContainer
                      TaskContainer      taskContainer

  @Override
  void applyPlugin() {
    taskContainer.withType(Groovydoc) { Groovydoc groovydocTask ->

      Jar groovydocJarTask = taskContainer.maybeCreate(GROOVYDOC_JAR_TASK, Jar)

      groovydocJarTask.with {
        dependsOn taskContainer.getByName('groovydoc')
        from groovydocTask.getDestinationDir()
        setClassifier 'groovydoc'
        setGroup 'build'
      }

    }

  }
}
