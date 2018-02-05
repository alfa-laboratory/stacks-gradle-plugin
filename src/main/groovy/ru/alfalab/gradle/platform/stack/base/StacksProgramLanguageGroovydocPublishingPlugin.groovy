package ru.alfalab.gradle.platform.stack.base

import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.dsl.ArtifactHandler
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.publish.PublicationContainer
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.TaskContainer
import ru.alfalab.gradle.platform.stack.api.ExtensionContainerAware
import ru.alfalab.gradle.platform.stack.api.TaskContainerAware

/**
 * @author tolkv
 * @version 21/12/2017
 */
@CompileStatic
class StacksProgramLanguageGroovydocPublishingPlugin extends StacksAbstractPlugin implements TaskContainerAware, ExtensionContainerAware {
  public static final String STACKS_GROOVY_DOC_ARCHIVE_CONFIGURATION = 'stacksGroovyDocArchive'

  ExtensionContainer extensionContainer
  TaskContainer      taskContainer

  @Override
  void applyPlugin() {
    pluginManager.apply StacksPublicationPlugin

    Task groovydocJarTask = taskContainer.findByName('groovydocJar')

    if (groovydocJarTask) {
      configurations.maybeCreate(STACKS_GROOVY_DOC_ARCHIVE_CONFIGURATION)
      artifacts { ArtifactHandler artifactHandler ->
        artifactHandler.add(STACKS_GROOVY_DOC_ARCHIVE_CONFIGURATION, groovydocJarTask)
      }
    }
  }
}
