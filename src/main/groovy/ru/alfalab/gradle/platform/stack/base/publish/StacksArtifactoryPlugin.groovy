package ru.alfalab.gradle.platform.stack.base.publish

import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.tasks.TaskContainer
import org.jfrog.gradle.plugin.artifactory.ArtifactoryPlugin
import org.jfrog.gradle.plugin.artifactory.dsl.ArtifactoryPluginConvention
import org.jfrog.gradle.plugin.artifactory.task.ArtifactoryTask
import ru.alfalab.gradle.platform.stack.api.ExtensionContainerAware
import ru.alfalab.gradle.platform.stack.api.PluginContainerAware
import ru.alfalab.gradle.platform.stack.api.TaskContainerAware
import ru.alfalab.gradle.platform.stack.base.StacksAbstractPlugin
import ru.alfalab.gradle.platform.stack.base.StacksExtension
import ru.alfalab.gradle.platform.stack.base.StacksPublicationPlugin

/**
 * @author tolkv
 * @version 27/12/2017
 */
@CompileStatic
class StacksArtifactoryPlugin extends StacksAbstractPlugin implements TaskContainerAware, ExtensionContainerAware {
  TaskContainer      taskContainer
  ExtensionContainer extensionContainer

  StacksArtifactoryRootConfigurer artifactiryRootConfigurer = new StacksArtifactoryRootConfigurer()

  @Override
  void applyPlugin() {
    rootProject.pluginManager.with {
      apply StacksPublicationPlugin
      apply 'com.jfrog.artifactory'
    }

    pluginManager.with {
      apply StacksPublicationPlugin
      apply 'com.jfrog.artifactory'
    }

    afterEvaluate {
      ArtifactoryPluginConvention artifactoryPluginConvention = rootProject.convention.plugins.get('artifactory') as ArtifactoryPluginConvention
      StacksPublishingConfiguration publishing = extensions.findByType(StacksExtension)?.publishing
      artifactiryRootConfigurer.configure artifactoryPluginConvention, publishing

      rootProject.tasks.withType(ArtifactoryTask) { ArtifactoryTask task ->
        task.dependsOn tasks.getByName('build')
      }

      if (rootProjectWithoutJava()) {
        rootProject.tasks.withType(ArtifactoryTask) { ArtifactoryTask task ->
          task.skip = true
        }
      }
    }
  }

  private boolean rootProjectWithoutJava() {
    !rootProject.pluginManager.hasPlugin('java')
  }
}


