package ru.alfalab.gradle.platform.stack.base

import groovy.transform.CompileStatic
import nebula.plugin.publishing.maven.MavenPublishPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author tolkv
 * @version 27/12/2017
 */
@CompileStatic
class StacksPublicationPlugin extends StacksAbstractPlugin {

  @Override
  void applyPlugin() {
    pluginManager.apply 'nebula.maven-publish'
  }

}
