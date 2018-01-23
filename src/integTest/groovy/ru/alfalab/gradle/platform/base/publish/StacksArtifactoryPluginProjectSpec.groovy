package ru.alfalab.gradle.platform.base.publish

import groovy.transform.CompileStatic
import nebula.test.ProjectSpec
import org.gradle.api.Project
import org.jfrog.gradle.plugin.artifactory.dsl.ArtifactoryPluginConvention
import ru.alfalab.gradle.platform.stack.base.StacksAbstractPlugin
import ru.alfalab.gradle.platform.stack.base.StacksBasePlugin
import ru.alfalab.gradle.platform.stack.base.publish.StacksArtifactoryPlugin

/**
 * @author tolkv
 * @version 27/12/2017
 */
class StacksArtifactoryPluginProjectSpec extends ProjectSpec {
  def 'should apply publishing plugins with'() {
    when:
      project.apply plugin: StacksArtifactoryPlugin

    then:
      project.pluginManager.hasPlugin 'stacks.artifactory'
      project.pluginManager.hasPlugin 'com.jfrog.artifactory'
      project.pluginManager.hasPlugin 'nebula.maven-publish'

      project.convention.findPlugin ArtifactoryPluginConvention
  }

  def 'should apply publishing plugins to root project in multiproject build'() {
    given:
      def sub0Prject = addSubproject('sub0')

    when:
      sub0Prject.apply plugin: StacksArtifactoryPlugin

    then:
      project.pluginManager.hasPlugin 'com.jfrog.artifactory'
      project.pluginManager.hasPlugin 'nebula.maven-publish'

      project.convention.findPlugin ArtifactoryPluginConvention
  }
}