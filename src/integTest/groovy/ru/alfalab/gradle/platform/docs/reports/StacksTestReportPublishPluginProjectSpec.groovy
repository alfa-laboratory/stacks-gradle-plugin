package ru.alfalab.gradle.platform.docs.reports

import nebula.test.PluginProjectSpec
import org.gradle.api.tasks.bundling.Zip
import org.jfrog.gradle.plugin.artifactory.task.ArtifactoryTask
import ru.alfalab.gradle.platform.stack.documentation.report.StacksTestReportsPublishPlugin

import static ru.alfalab.gradle.platform.stack.documentation.report.StacksTestReportsPublishPlugin.STACKS_TEST_REPORTS_ARCHIVE_CONFIGURATION

/**
 * @author tolkv
 * @version 27/04/2018
 */
class StacksTestReportPublishPluginProjectSpec extends PluginProjectSpec {

  def 'should configure archive for publish'() {
    when:
      project.plugins.apply 'java'
      project.plugins.apply getPluginName()

    then:
      Zip reportsZipTask = project.tasks.findByName(StacksTestReportsPublishPlugin.STACKS_TEST_REPORT_ARCHIVE_TASKNAME)
      reportsZipTask.classifier == 'testReport'
      reportsZipTask.inputs.files.findAll { it.path.contains '/reports/tests/test' }
      reportsZipTask.dependsOn.findAll { it.name == 'test' }.size() == 1
  }

  def 'should configure artifactory tasks for publish reports'() {
    when:
      project.plugins.apply 'java'
      project.plugins.apply 'com.jfrog.artifactory'
      project.plugins.apply getPluginName()

    then: 'configure publish archive with custom name and classifier'
      project.configurations.findByName(STACKS_TEST_REPORTS_ARCHIVE_CONFIGURATION).artifacts.findAll {
        it.name == 'should-configure-artifactory-tasks-for-publish-reports' && it.classifier == 'testReport'
      }.size() == 1

    and: 'configure artifactory task'
      def task = project.tasks.findByName('artifactoryPublish') as ArtifactoryTask

      !task.skip
      !task.publishIvy

      task.publishConfigs.findAll {
        it.name == STACKS_TEST_REPORTS_ARCHIVE_CONFIGURATION
      }.size() == 1
  }

  @Override
  String getPluginName() {
    return 'stacks.report.test'
  }
}
