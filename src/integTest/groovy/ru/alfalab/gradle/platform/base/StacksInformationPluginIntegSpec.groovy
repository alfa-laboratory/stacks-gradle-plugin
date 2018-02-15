package ru.alfalab.gradle.platform.base

import ru.alfalab.gradle.platform.stack.base.StacksProgramLanguageGroovyPlugin
import ru.alfalab.gradle.platform.stack.base.publish.StacksArtifactoryPlugin
import ru.alfalab.gradle.platform.tests.base.StacksSimpleIntegrationSpec

import java.util.zip.ZipFile

import static org.apache.commons.lang.StringUtils.isNotBlank

/**
 * @author tolkv
 * @version 29/12/2017
 */
class StacksInformationPluginIntegSpec extends StacksSimpleIntegrationSpec {

  def 'should not write manifest properties without empty values'() {
    given:
      buildFile << """
    ${applyPlugin(StacksProgramLanguageGroovyPlugin)}
    ${applyPlugin(StacksArtifactoryPlugin)}
    group = 'ru.alfalab.gradle.stacks.infoplugin.escape.test'
    version = '0.0.1'
    
    task checkProperties() {
      doLast {
        logger.quiet 'checkthis-->' + tasks.artifactoryPublish.properties
      }
    }
    """.stripMargin().stripIndent()

    expect: 'should not contain ; char and more than 5 default properties'
      def result = runTasksSuccessfully('checkProperties', 'build')

      !result.standardOutput.split('\n')
             .find { it.contains('checkthis-->') }.contains(';')

      def jarFile = file('build/libs/should-not-write-manifest-properties-without-empty-values-0.0.1.jar')
      def zipFile = new ZipFile(jarFile)


      def entry = zipFile.getEntry('META-INF/should-not-write-manifest-properties-without-empty-values.properties')

      Properties properties = new Properties()
      properties.load(zipFile.getInputStream(entry))

      properties
          .findAll { isNotBlank it.value as String }
          .size() > 5

  }
}
