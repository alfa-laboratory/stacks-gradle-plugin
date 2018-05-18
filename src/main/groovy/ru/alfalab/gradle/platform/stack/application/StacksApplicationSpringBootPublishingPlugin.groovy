package ru.alfalab.gradle.platform.stack.application

import groovy.transform.CompileStatic
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.publish.maven.tasks.GenerateMavenPom
import org.gradle.api.tasks.TaskContainer
import org.jfrog.build.extractor.clientConfiguration.ArtifactSpec
import org.jfrog.gradle.plugin.artifactory.ArtifactoryPlugin
import org.jfrog.gradle.plugin.artifactory.dsl.ArtifactoryPluginConvention
import org.jfrog.gradle.plugin.artifactory.task.ArtifactoryTask
import ru.alfalab.gradle.platform.stack.api.ExtensionContainerAware
import ru.alfalab.gradle.platform.stack.api.TaskContainerAware
import ru.alfalab.gradle.platform.stack.application.spring.SpringBootVersionsHelper
import ru.alfalab.gradle.platform.stack.base.StacksAbstractPlugin
import ru.alfalab.gradle.platform.stack.base.StacksExtension
import ru.alfalab.gradle.platform.stack.base.publish.ArtifactoryTaskMergePropertiesConfigurer

import static java.lang.String.format
import static ru.alfalab.gradle.platform.stack.application.ArtifactoryConventionSpringBootDynamicConfigurator.configureArtifactoryConventionForSpringBoot

/**
 * @author tolkv
 * @version 20/12/2017
 */
@CompileStatic
class StacksApplicationSpringBootPublishingPlugin extends StacksAbstractPlugin implements ExtensionContainerAware, TaskContainerAware {
  TaskContainer      taskContainer
  ExtensionContainer extensionContainer

  @Override
  void applyPlugin() {
    configureArtifactoryTaskWithPublicationsLazyInit { ArtifactoryTask t ->

      StacksExtension stacksExtension = extensionContainer.findByType(StacksExtension)
      StacksApplicationConfiguration config = stacksExtension.applicationConfig
      String applicationClassifier = config.classifierProvider.getOrElse('app')
      String title = config.titleProvider.getOrElse(rootProject.name)
      String description = project.description ? project.description : rootProject.description
      def artifactNameInDeploymentId = rootProject.name != project.name ? rootProject.name + '/' + project.name : project.name
      String deploymentId = format('%s:%s:%s', rootProject.group, "${artifactNameInDeploymentId}", applicationClassifier)

      taskContainer.withType(RepackageTask) { RepackageTask repackageTask ->
        t.dependsOn repackageTask
      }

      t.with {
        skip = false
      }

      t.dependsOn tasks.withType(GenerateMavenPom)
      t.dependsOn tasks.findByName('build')

      def commonProperties = [
          'platform.display-name'  : title,
          'platform.artifact.group': project.group?.toString(),
          'platform.artifact.name' : project.name,
          'platform'               : 'true',
          'version'                : project.version?.toString(),
          'platform.app'           : 'true'
      ]

      if (description) {
        commonProperties << ['platform.description': description]
      }

      def javadocProperties = [
          'platform.artifact-type': 'javadoc',
          'platform.label'        : 'doc',
      ]

      def groovydocProperties = []

      def sourcesProperties = [
          'platform.artifact-type': 'sourcecode',
          'platform.label'        : 'source',
      ]

      def serviceProperties = [
          'platform.deployment.app-name': rootProject.name,
          'platform.deployment.id'      : deploymentId,
          'platform.artifact-type'      : 'service',
          'platform.label'              : 'api',
      ]

      ArtifactoryPluginConvention artifactoryConvention = convention.findPlugin(ArtifactoryPluginConvention)

      configureArtifactoryConventionForSpringBoot(artifactoryConvention, t)
          .withAppClassifier(applicationClassifier)
          .withAppArtifactProperties(commonProperties + serviceProperties)
          .withGroovydocArtifactProperties(commonProperties + groovydocProperties)
          .withJavadocArtifactProperties(commonProperties + javadocProperties)
          .withSourcesArtifactProperties(commonProperties + sourcesProperties)
          .finish()

      rootProject.allprojects { Project currentProject ->
        currentProject.tasks.withType(ArtifactoryTask) { ArtifactoryTask artifactoryTask ->
          new ArtifactoryTaskMergePropertiesConfigurer(artifactoryTask).putAllSpecTo([
              ArtifactSpec.builder()
                  .artifactNotation('*:*:*:*@*')
                  .configuration('all')
                  .properties(['platform.service.id': deploymentId])
                  .build()
          ])
        }
      }
    }
  }

  void configureArtifactoryTaskWithPublicationsLazyInit(Action<ArtifactoryTask> closure) {
    plugins.withType(ArtifactoryPlugin) { ArtifactoryPlugin p ->
      debug 'configure publications and meta info for spring boot application project'
      plugins.withType(MavenPublishPlugin) {
        taskContainer.withType(ArtifactoryTask, closure)
      }

    }
  }

}