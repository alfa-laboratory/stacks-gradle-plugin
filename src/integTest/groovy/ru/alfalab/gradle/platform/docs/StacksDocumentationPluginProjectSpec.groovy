package ru.alfalab.gradle.platform.docs

import nebula.test.PluginProjectSpec
import org.jfrog.gradle.plugin.artifactory.task.ArtifactoryTask
import ru.alfalab.gradle.platform.stack.documentation.asciidoctor.StacksAsciidoctorDynamicPlugin
import ru.alfalab.gradle.platform.stack.documentation.asciidoctor.StacksAsciidoctorPublishPlugin

/**
 * @author tolkv
 * @version 27/04/2018
 */
class StacksDocumentationPluginProjectSpec extends PluginProjectSpec {

  def 'should apply asciidoctor subplugin to project with asciidoctor'() {
    when:
      project.plugins.apply 'org.asciidoctor.convert'
      project.plugins.apply getPluginName()

    then: 'should apply plugin and add archive task and dont add special configuration without artifactory plugin'
      project.plugins.hasPlugin(StacksAsciidoctorDynamicPlugin)
      project.tasks.findByName(StacksAsciidoctorPublishPlugin.STACKS_ASCIIDOCTOR_DOC_ARCHIVE_TASKNAME)
      !project.configurations.findByName(StacksAsciidoctorPublishPlugin.STACKS_ASCIIDOCTOR_DOC_ARCHIVE_CONFIGURATION)
  }

  def 'should not apply asciidoctor subplugin to project with asciidoctor'() {
    when:
      project.plugins.apply getPluginName()

    then:
      !project.plugins.hasPlugin(StacksAsciidoctorDynamicPlugin)
  }

  def 'should configure publish task and configuration'() {
    when:
      project.plugins.apply 'org.asciidoctor.convert'
      project.plugins.apply 'com.jfrog.artifactory'
      project.plugins.apply getPluginName()

    then: 'should add archive task and special configuration'
      project.tasks.findByName(StacksAsciidoctorPublishPlugin.STACKS_ASCIIDOCTOR_DOC_ARCHIVE_TASKNAME)
      project.configurations.findByName(StacksAsciidoctorPublishPlugin.STACKS_ASCIIDOCTOR_DOC_ARCHIVE_CONFIGURATION)

    and: 'configure publish archive with custom name and classifier'
      project.configurations.findByName('stacksDocsAsciidoctorArchive').artifacts.findAll {
        it.name == 'should-configure-publish-task-and-configuration' && it.classifier == 'docs'
      }.size() == 1

    and: 'configure artifactory task'
      def task = project.tasks.findByName('artifactoryPublish') as ArtifactoryTask

      !task.skip
      !task.publishIvy

      task.publishConfigs.findAll {
        it.name == StacksAsciidoctorPublishPlugin.STACKS_ASCIIDOCTOR_DOC_ARCHIVE_CONFIGURATION
      }.size() == 1
  }

  @Override
  String getPluginName() {
    return 'stacks.doc'
  }
}
