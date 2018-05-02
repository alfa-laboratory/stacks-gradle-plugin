package ru.alfalab.gradle.platform.docs.reports

import nebula.test.PluginProjectSpec
import org.gradle.api.tasks.bundling.Zip
import ru.alfalab.gradle.platform.stack.documentation.report.StacksTestReportsPublishPlugin

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

  @Override
  String getPluginName() {
    return 'stacks.report.test'
  }
}
