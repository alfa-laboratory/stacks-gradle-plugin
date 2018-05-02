package ru.alfalab.gradle.platform.stack.documentation.report

import org.gradle.api.artifacts.ConfigurablePublishArtifact
import org.gradle.api.artifacts.dsl.ArtifactHandler
import org.gradle.api.file.CopySpec
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.bundling.Zip
import org.gradle.api.tasks.testing.Test
import org.jfrog.gradle.plugin.artifactory.ArtifactoryPlugin
import org.jfrog.gradle.plugin.artifactory.task.ArtifactoryTask
import ru.alfalab.gradle.platform.stack.api.PluginContainerAware
import ru.alfalab.gradle.platform.stack.api.TaskContainerAware
import ru.alfalab.gradle.platform.stack.base.StacksAbstractPlugin

class StacksTestReportsPublishPlugin extends StacksAbstractPlugin implements PluginContainerAware, TaskContainerAware {
  public static final String STACKS_TEST_REPORTS_ARCHIVE_CONFIGURATION = 'stacksTestReportsArchive'
  public static final String STACKS_TEST_REPORT_ARCHIVE_TASKNAME       = 'stacksTestReportDistZip'
  public static final String TEST_REPORT_CLASSIFIER                    = 'testReport'

  PluginContainer pluginContainer
  TaskContainer   taskContainer

  @Override
  void applyPlugin() {
    taskContainer.withType(Test) { Test testTask ->
      Zip stacksTestReportDistZipTask = taskContainer.maybeCreate(STACKS_TEST_REPORT_ARCHIVE_TASKNAME, Zip)

      if (testTask?.reports?.html?.destination) {
        stacksTestReportDistZipTask.with { CopySpec copySpec ->
          inputs.files testTask.outputs
          classifier = TEST_REPORT_CLASSIFIER
          copySpec.from(testTask.reports.html.destination)
        }

        stacksTestReportDistZipTask.dependsOn(testTask)

        pluginContainer.withType(ArtifactoryPlugin) {
          configurations.maybeCreate(STACKS_TEST_REPORTS_ARCHIVE_CONFIGURATION)
          artifacts { ArtifactHandler artifactHandler ->
            artifactHandler.add(STACKS_TEST_REPORTS_ARCHIVE_CONFIGURATION, stacksTestReportDistZipTask) {
              ConfigurablePublishArtifact configurablePublishArtifact ->
                configurablePublishArtifact.classifier = TEST_REPORT_CLASSIFIER
            }
          }
        }

        taskContainer.withType(ArtifactoryTask) { ArtifactoryTask task ->
          task.skip = false
          task.publishIvy = false
          task.publishConfigs(STACKS_TEST_REPORTS_ARCHIVE_CONFIGURATION)
        }

      } else {
        logger.warn 'stacks plugin can\'t configure stacksTestReportDistZip task. Html reports not found'
      }

    }
  }

}