package ru.alfalab.gradle.platform.stack.application

import org.gradle.api.publish.maven.MavenPublication
import org.jfrog.build.extractor.clientConfiguration.ArtifactSpec
import org.jfrog.gradle.plugin.artifactory.dsl.ArtifactoryPluginConvention
import org.jfrog.gradle.plugin.artifactory.task.ArtifactoryTask
import ru.alfalab.gradle.platform.stack.base.publish.ArtifactoryTaskMergePropertiesConfigurer

class ArtifactoryConventionSpringBootDynamicConfigurator {
  private ArtifactoryPluginConvention artifactoryConvention
  private ArtifactoryTask             task

  private String              appClassifier = 'app'
  private Map<String, String> appArtifactProperties
  private Map<String, String> groovydocArtifactProperties
  private Map<String, String> javadocArtifactProperties
  private Map<String, String> sourcesArtifactProperties

  ArtifactoryConventionSpringBootDynamicConfigurator(
      ArtifactoryPluginConvention artifactoryConvention, ArtifactoryTask task) {
    this.artifactoryConvention = artifactoryConvention
    this.task = task
    this
  }

  static ArtifactoryConventionSpringBootDynamicConfigurator configureArtifactoryConventionForSpringBoot(
      ArtifactoryPluginConvention convention, ArtifactoryTask task) {
    return new ArtifactoryConventionSpringBootDynamicConfigurator(convention, task)
  }

  ArtifactoryConventionSpringBootDynamicConfigurator withAppClassifier(String appClassifier) {
    this.appClassifier = appClassifier
    this
  }

  ArtifactoryConventionSpringBootDynamicConfigurator withAppArtifactProperties(
      Map<String, String> appArtifactProperties) {
    this.appArtifactProperties = filterBlankProperties(appArtifactProperties)
    this
  }

  ArtifactoryConventionSpringBootDynamicConfigurator withGroovydocArtifactProperties(
      Map<String, String> groovydocArtifactProperties) {
    this.groovydocArtifactProperties = filterBlankProperties(groovydocArtifactProperties)
    this
  }

  ArtifactoryConventionSpringBootDynamicConfigurator withJavadocArtifactProperties(
      Map<String, String> javadocArtifactProperties) {
    this.javadocArtifactProperties = filterBlankProperties(javadocArtifactProperties)
    this
  }

  ArtifactoryConventionSpringBootDynamicConfigurator withSourcesArtifactProperties(
      Map<String, String> sourcesArtifactProperties) {
    this.sourcesArtifactProperties = filterBlankProperties(sourcesArtifactProperties)
    this
  }

  private static Map<String, String> filterBlankProperties(Map<String, String> properties) {
    properties.findAll { it.value?.trim() }
  }

  ArtifactoryPluginConvention getArtifactoryConvention() {
    return artifactoryConvention
  }

  Map<String, String> getAppArtifactProperties() {
    return appArtifactProperties
  }

  Map<String, String> getGroovydocArtifactProperties() {
    return groovydocArtifactProperties
  }

  Map<String, String> getJavadocArtifactProperties() {
    return javadocArtifactProperties
  }

  Map<String, String> getSourcesArtifactProperties() {
    return sourcesArtifactProperties
  }

  void finish() {
    task.skip = false
    def project = artifactoryConvention.project

    project.plugins.withType(StacksApplicationSpringBootTaskPlugin) {
      project.publishing {
        publications {
          bootJava(MavenPublication) {
            artifact project.tasks.findByPath('bootJar') //TODO needs backward compatibility with SP1
            task.publications(bootJava)
          }
        }
      }
    }

    //TODO super mega hack
    new ArtifactoryTaskMergePropertiesConfigurer(task).putAllSpecTo([
        ArtifactSpec.builder()
                    .artifactNotation('*:*:*:' + appClassifier + '@*')
                    .configuration('bootJava')
                    .properties(appArtifactProperties)
                    .build(),
        ArtifactSpec.builder()
                    .artifactNotation('*:*:*:groovydoc@*')
                    .configuration('all')
                    .properties(groovydocArtifactProperties)
                    .build(),
        ArtifactSpec.builder()
                    .artifactNotation('*:*:*:javadoc@*')
                    .configuration('nebula')
                    .properties(javadocArtifactProperties)
                    .build(),
        ArtifactSpec.builder()
                    .artifactNotation('*:*:*:sources@*')
                    .configuration('nebula')
                    .properties(sourcesArtifactProperties)
                    .build()
    ])

  }

}
