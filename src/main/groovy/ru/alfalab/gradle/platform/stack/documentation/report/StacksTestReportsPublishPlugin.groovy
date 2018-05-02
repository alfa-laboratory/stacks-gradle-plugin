package ru.alfalab.gradle.platform.stack.documentation.report

import org.gradle.api.file.CopySpec
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.bundling.Zip
import org.gradle.api.tasks.testing.Test
import ru.alfalab.gradle.platform.stack.api.PluginContainerAware
import ru.alfalab.gradle.platform.stack.api.TaskContainerAware
import ru.alfalab.gradle.platform.stack.base.StacksAbstractPlugin

class StacksTestReportsPublishPlugin extends StacksAbstractPlugin implements PluginContainerAware, TaskContainerAware {
  public static final String STACKS_TEST_REPORT_ARCHIVE_TASKNAME = 'stacksTestReportDistZip'

  PluginContainer pluginContainer
  TaskContainer   taskContainer

  @Override
  void applyPlugin() {
    taskContainer.withType(Test) { Test testTask ->
      Zip stacksTestReportDistZipTask = taskContainer.maybeCreate(STACKS_TEST_REPORT_ARCHIVE_TASKNAME, Zip)

      if (testTask?.reports?.html?.destination) {
        stacksTestReportDistZipTask.with { CopySpec copySpec ->
          inputs.files testTask.outputs
          classifier = 'testReport'
          copySpec.from(testTask.reports.html.destination)
        }

        stacksTestReportDistZipTask.dependsOn(testTask)
      } else {
        logger.warn 'stacks plugin can\'t configure stacksTestReportDistZip task. Html reports not found'
      }

    }
  }

}