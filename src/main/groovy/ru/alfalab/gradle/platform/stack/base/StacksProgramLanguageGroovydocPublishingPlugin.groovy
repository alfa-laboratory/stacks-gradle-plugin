package ru.alfalab.gradle.platform.stack.base

import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.api.Task
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
  ExtensionContainer extensionContainer
  TaskContainer      taskContainer

  @Override
  void applyPlugin() {
    pluginManager.apply StacksPublicationPlugin

    Task groovydocJarTask = taskContainer.findByName('groovydocJar')

    if (groovydocJarTask) {
      extensionContainer.configure(PublishingExtension) { PublishingExtension publishingExtension ->
        publishingExtension.publications { PublicationContainer publicationContainer ->
          publicationContainer.maybeCreate('nebula', MavenPublication)
                              .artifact(groovydocJarTask)
        }
      }
    }
  }
}
