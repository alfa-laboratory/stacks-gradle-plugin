package ru.alfalab.gradle.platform.app

import nebula.test.ProjectSpec
import org.jfrog.gradle.plugin.artifactory.task.ArtifactoryTask

/**
 * @see StacksApplicationPluginPublishingIntegSpec for check artifactory metadata in publication phase
 * @author tolkv
 * @version 22/12/2017
 */
class StacksProgramLanguageGroovyPublishPluginProjectSpec extends ProjectSpec {

  def 'should add configuration for groovydoc artifact'() {
    when:
      project.apply plugin: 'stacks.lang.groovy'
      project.apply plugin: 'stacks.artifactory'

    then:
      noExceptionThrown()

      def configuredArtifacts = project.configurations.findByName('stacksGroovyDocArchive').artifacts

      configuredArtifacts.size() == 1
      configuredArtifacts.files.any { it.name.contains 'groovydoc' }

      def ap = project.tasks.findByName('artifactoryPublish') as ArtifactoryTask
      ap.publishConfigs.find { it.name == 'stacksGroovyDocArchive' }
  }

}
