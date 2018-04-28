package ru.alfalab.gradle.platform.stack.documentation.asciidoctor

import groovy.transform.CompileStatic
import org.asciidoctor.gradle.AsciidoctorTask
import org.gradle.api.artifacts.ConfigurablePublishArtifact
import org.gradle.api.artifacts.dsl.ArtifactHandler
import org.gradle.api.file.CopySpec
import org.gradle.api.internal.TaskOutputsInternal
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.bundling.Zip
import org.jfrog.gradle.plugin.artifactory.ArtifactoryPlugin
import org.jfrog.gradle.plugin.artifactory.task.ArtifactoryTask
import ru.alfalab.gradle.platform.stack.api.PluginContainerAware
import ru.alfalab.gradle.platform.stack.api.TaskContainerAware
import ru.alfalab.gradle.platform.stack.base.StacksAbstractPlugin

/**
 * @author tolkv
 * @version 27/04/2018
 */
@CompileStatic
class StacksAsciidoctorPublishPlugin extends StacksAbstractPlugin implements PluginContainerAware, TaskContainerAware {
  public static final String STACKS_ASCIIDOCTOR_DOC_ARCHIVE_CONFIGURATION = 'stacksDocsAsciidoctorArchive'
  public static final String STACKS_ASCIIDOCTOR_DOC_ARCHIVE_TASKNAME      = 'stacksDocsAsciidoctorDistZip'

  PluginContainer pluginContainer
  TaskContainer   taskContainer

  @Override
  void applyPlugin() {

    pluginContainer.withId('org.asciidoctor.convert') {
      taskContainer.withType(AsciidoctorTask) { AsciidoctorTask asciidoctorTask ->
        Zip asciidocDistZip = taskContainer.maybeCreate(STACKS_ASCIIDOCTOR_DOC_ARCHIVE_TASKNAME, Zip)

        asciidocDistZip.with { CopySpec copySpec ->
          inputs.files asciidoctorTask.outputs
          classifier = 'docs'
          copySpec.from(extractAsciidoctorTaskOutput(asciidoctorTask))
        }

        pluginContainer.withType(ArtifactoryPlugin) {

          configurations.maybeCreate(STACKS_ASCIIDOCTOR_DOC_ARCHIVE_CONFIGURATION)

          project.artifacts { ArtifactHandler artifactHandler ->
            artifactHandler.add(STACKS_ASCIIDOCTOR_DOC_ARCHIVE_CONFIGURATION, asciidocDistZip) {
              ConfigurablePublishArtifact configurablePublishArtifact
                ->
                configurablePublishArtifact.classifier = 'docs'
            }

            taskContainer.withType(ArtifactoryTask) { ArtifactoryTask task ->
              task.skip = false
              task.publishIvy = false
              task.publishConfigs(STACKS_ASCIIDOCTOR_DOC_ARCHIVE_CONFIGURATION)
            }
          }
        }
      }
    }


  }

  private Object extractAsciidoctorTaskOutput(AsciidoctorTask asciidoctorTask) {
    if (asciidoctorTask.backend == 'html5' || 'html5' in asciidoctorTask.backends) {
      return project.file(asciidoctorTask.outputDir.path + '/html5')
    }
    return asciidoctorTask.outputs
  }
}
