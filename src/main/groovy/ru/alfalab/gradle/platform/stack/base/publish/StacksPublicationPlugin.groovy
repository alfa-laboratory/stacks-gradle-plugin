package ru.alfalab.gradle.platform.stack.base.publish

import groovy.transform.CompileStatic
import nebula.plugin.publishing.maven.*
import nebula.plugin.publishing.verification.PublishVerificationPlugin
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import ru.alfalab.gradle.platform.stack.api.PluginContainerAware
import ru.alfalab.gradle.platform.stack.base.StacksAbstractPlugin

/**
 * @author tolkv
 * @version 27/12/2017
 */
@CompileStatic
class StacksPublicationPlugin extends StacksAbstractPlugin implements PluginContainerAware {
  PluginContainer pluginContainer

  @Override
  void applyPlugin() {
    pluginContainer.with {

      //Gradle
      apply MavenPublishPlugin

      //Stacks
      apply MavenBasePublishPlugin
      apply MavenCompileOnlyPlugin
      apply MavenResolvedDependenciesPlugin
      apply MavenDeveloperPlugin
      apply MavenManifestPlugin
      apply MavenScmPlugin

      //Nebula
      apply PublishVerificationPlugin
    }
  }

}
