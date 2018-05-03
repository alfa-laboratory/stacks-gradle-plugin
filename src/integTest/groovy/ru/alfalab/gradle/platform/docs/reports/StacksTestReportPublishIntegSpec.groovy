package ru.alfalab.gradle.platform.docs.reports

import nebula.test.IntegrationSpec
import ru.alfalab.gradle.platform.stack.documentation.report.StacksTestReportsPublishPlugin

import java.util.zip.ZipFile

/**
 * @author tolkv
 * @version 27/04/2018
 */
class StacksTestReportPublishIntegSpec extends IntegrationSpec {

  def 'should configure archive for publish'() {
    given:
      buildFile << '''  
      group = 'stacks.report.test.publish'
      apply plugin: 'java'
      apply plugin: 'stacks.report.test'
      
      dependencies {
        testCompile 'junit:junit:4.12'
      }
'''

      writeHelloWorld('stacks.report.test.publish')
      writeUnitTest(false)

    when:
      def result = runTasksSuccessfully(StacksTestReportsPublishPlugin.STACKS_TEST_REPORT_ARCHIVE_TASKNAME)

    then:
      result.wasExecuted('test')

      findIndexHtmlInRootTestReportZip('should-configure-archive-for-publish-testReport')
  }

  def 'should be independent on plugin order'() {
    given:
      buildFile << '''  
      group = 'stacks.report.test.publish'
      apply plugin: 'stacks.report.test'
      apply plugin: 'java'
      
      dependencies {
        testCompile 'junit:junit:4.12'
      }
'''

      writeHelloWorld('stacks.report.test.publish')
      writeUnitTest(false)

    when:
      def result = runTasksSuccessfully(StacksTestReportsPublishPlugin.STACKS_TEST_REPORT_ARCHIVE_TASKNAME)

    then:
      result.wasExecuted('test')

      findIndexHtmlInRootTestReportZip('should-be-independent-on-plugin-order-testReport')
  }

  boolean findIndexHtmlInRootTestReportZip(String archiveName) {
    boolean result = false

    new ZipFile(
        projectDir.path + "/build/distributions/${archiveName}.zip").withCloseable {
      result = it.entries().findAll { it.name == 'index.html' }.size() == 1
    }

    return result
  }

}
