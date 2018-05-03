package ru.alfalab.gradle.platform.stack.base

import groovy.transform.CompileStatic
import org.gradle.api.Task
import org.gradle.api.artifacts.dsl.ArtifactHandler
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.tasks.TaskContainer
import org.jfrog.build.extractor.clientConfiguration.ArtifactSpec
import org.jfrog.gradle.plugin.artifactory.ArtifactoryPlugin
import org.jfrog.gradle.plugin.artifactory.task.ArtifactoryTask
import ru.alfalab.gradle.platform.stack.api.PluginContainerAware
import ru.alfalab.gradle.platform.stack.api.TaskContainerAware
import ru.alfalab.gradle.platform.stack.base.publish.ArtifactoryTaskMergePropertiesConfigurer
import ru.alfalab.gradle.platform.stack.base.publish.StacksPublicationPlugin

/**
 * @author tolkv
 * @version 21/12/2017
 */
@CompileStatic
class StacksProgramLanguageGroovydocPublishingPlugin extends StacksAbstractPlugin implements TaskContainerAware, PluginContainerAware {
  public static final String STACKS_GROOVY_DOC_ARCHIVE_CONFIGURATION = 'stacksGroovyDocArchive'

  PluginContainer pluginContainer
  TaskContainer   taskContainer

  @Override
  void applyPlugin() {
    pluginManager.apply StacksPublicationPlugin

    plugins.withType(StacksProgramLanguageGroovydocPlugin) {
      Task groovydocJarTask = taskContainer.findByName('groovydocJar')

      if (groovydocJarTask) {
        configurations.maybeCreate(STACKS_GROOVY_DOC_ARCHIVE_CONFIGURATION)
        artifacts { ArtifactHandler artifactHandler ->
          artifactHandler.add(STACKS_GROOVY_DOC_ARCHIVE_CONFIGURATION, groovydocJarTask)
        }
      }
      pluginContainer.withType(ArtifactoryPlugin) {
        taskContainer.withType(ArtifactoryTask) { ArtifactoryTask task ->
          task.skip = false
          task.publishConfigs(STACKS_GROOVY_DOC_ARCHIVE_CONFIGURATION)
          new ArtifactoryTaskMergePropertiesConfigurer(task).putAllSpecTo([
              ArtifactSpec.builder()
                          .artifactNotation('*:*:*:groovydoc@*')
                          .configuration(STACKS_GROOVY_DOC_ARCHIVE_CONFIGURATION)
                          .properties(['platform.artifact-type': 'groovydoc',
                                       'platform.label'        : 'doc'])
                          .build()
          ])
        }

      }
    }
  }
}
