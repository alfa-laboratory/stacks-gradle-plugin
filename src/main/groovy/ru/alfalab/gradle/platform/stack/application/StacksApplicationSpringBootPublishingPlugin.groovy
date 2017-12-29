package ru.alfalab.gradle.platform.stack.application

import groovy.transform.CompileStatic
import org.gradle.api.Action
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.publish.PublicationContainer
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.tasks.TaskContainer
import org.jfrog.build.extractor.clientConfiguration.ArtifactSpec
import org.jfrog.gradle.plugin.artifactory.ArtifactoryPlugin
import org.jfrog.gradle.plugin.artifactory.task.ArtifactoryTask
import org.springframework.boot.gradle.repackage.RepackageTask
import ru.alfalab.gradle.platform.stack.api.ExtensionContainerAware
import ru.alfalab.gradle.platform.stack.api.PluginContainerAware
import ru.alfalab.gradle.platform.stack.api.TaskContainerAware
import ru.alfalab.gradle.platform.stack.base.StacksAbstractPlugin
import ru.alfalab.gradle.platform.stack.base.StacksExtension
import ru.alfalab.gradle.platform.stack.base.StacksPublicationPlugin

import static java.lang.String.format

/**
 * @author tolkv
 * @version 20/12/2017
 */
class StacksApplicationSpringBootPublishingPlugin extends StacksAbstractPlugin implements ExtensionContainerAware, TaskContainerAware {
  public static final String DEFAULT_PUBLISH_CONFIGURATION_NAME = 'nebula'

  TaskContainer      taskContainer
  ExtensionContainer extensionContainer

  @Override
  @CompileStatic
  void applyPlugin() {
    configureArtifactoryTaskWithPublicationsLazyInit { ArtifactoryTask t ->

      StacksExtension stacksExtension = extensionContainer.findByType(StacksExtension)
      StacksApplicationConfiguration config = stacksExtension.applicationConfig
      String classifier = config.classifierProvider.getOrElse('app')
      String title = config.titleProvider.getOrElse(rootProject.name)
      String description = project.description ? project.description : rootProject.description
      String deploymentId = format('%s:%s:%s', project.group, project.name, classifier)

      taskContainer.withType(RepackageTask) { RepackageTask repackageTask ->
        t.dependsOn repackageTask
      }

      t.with {
        skip = false
        publications(DEFAULT_PUBLISH_CONFIGURATION_NAME)

        // because build-info-extractor remove all artifact specs from ArtifactoryTask, which was set by artifactoryPublish.properties dsl
        // i hope, this is a temporary code, but who knows...
        doFirst('addArtifactSpecAgain') { ArtifactoryTask actionTask ->

          def commonProperties = [
              'platform.display-name'  : title,
              'platform.artifact.group': project.group?.toString(),
              'platform.artifact.name' : project.name,
              'platform'               : 'true'
          ]
          if (description) {
            commonProperties << ['platform.description': description]
          }
          def javadocProperties = [
              'platform.artifact-type': 'javadoc',
              'platform.label'        : 'doc',
          ]

          def groovydocProperties = [
              'platform.artifact-type': 'groovydoc',
              'platform.label'        : 'doc',
          ]

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

          ArtifactSpec springAppArtifactSpec = ArtifactSpec.builder()
                                                           .configuration(DEFAULT_PUBLISH_CONFIGURATION_NAME)
                                                           .name('*')
                                                           .group('*')
                                                           .version('*')
                                                           .classifier(classifier)
                                                           .properties(commonProperties + serviceProperties)
                                                           .build()

          ArtifactSpec springAppGroovydocArtifactSpec = ArtifactSpec.builder()
                                                                    .configuration(DEFAULT_PUBLISH_CONFIGURATION_NAME)
                                                                    .name('*')
                                                                    .group('*')
                                                                    .version('*')
                                                                    .classifier('groovydoc')
                                                                    .properties(filterNull(commonProperties + groovydocProperties))
                                                                    .build()

          ArtifactSpec springAppJavadocArtifactSpec = ArtifactSpec.builder()
                                                                  .configuration(DEFAULT_PUBLISH_CONFIGURATION_NAME)
                                                                  .name('*')
                                                                  .group('*')
                                                                  .version('*')
                                                                  .classifier('javadoc')
                                                                  .properties(commonProperties + javadocProperties)
                                                                  .build()

          ArtifactSpec springAppSourcesArtifactSpec = ArtifactSpec.builder()
                                                                  .configuration(DEFAULT_PUBLISH_CONFIGURATION_NAME)
                                                                  .name('*')
                                                                  .group('*')
                                                                  .version('*')
                                                                  .classifier('sources')
                                                                  .properties(commonProperties + sourcesProperties)
                                                                  .build()

          //TODO ArtifactoryTask#artifactSpec marked as 'only for test'. Need to avoid this
          actionTask.artifactSpecs.addAll([springAppArtifactSpec,
                                           springAppGroovydocArtifactSpec,
                                           springAppJavadocArtifactSpec,
                                           springAppSourcesArtifactSpec,
          ])


        }

      }

    }
  }

  private Map<String, String> filterNull(Map<String, String> properties) {
    properties.findAll { it.value != null }
  }


  void configureArtifactoryTaskWithPublicationsLazyInit(Action<ArtifactoryTask> closure) {
    plugins.withType(ArtifactoryPlugin) { ArtifactoryPlugin p ->
      debug 'configure publications and meta info for spring boot application project'
      afterEvaluate {
        extensionContainer.configure(PublishingExtension) { PublishingExtension publishingExtension ->
          publishingExtension.publications { PublicationContainer publicationContainer ->
            taskContainer.withType(ArtifactoryTask, closure)
          }
        }
      }
    }
  }

}