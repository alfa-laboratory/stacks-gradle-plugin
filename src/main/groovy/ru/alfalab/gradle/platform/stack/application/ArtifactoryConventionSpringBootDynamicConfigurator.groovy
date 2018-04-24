package ru.alfalab.gradle.platform.stack.application

import org.jfrog.build.extractor.clientConfiguration.ArtifactSpec
import org.jfrog.build.extractor.clientConfiguration.ArtifactSpecs
import org.jfrog.gradle.plugin.artifactory.ArtifactoryPlugin
import org.jfrog.gradle.plugin.artifactory.dsl.ArtifactoryPluginConvention
import org.jfrog.gradle.plugin.artifactory.task.ArtifactoryTask

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
    artifactoryConvention.artifactory {
      publish {
        defaults {
          skip = false
          publications('nebula')
        }
      }
    }

    task.skip = false
    artifactoryConvention.project.publishing {
      publications {
        task.publications(nebula)
      }
    }

    //TOOD super mega hack
    task.doFirst {
      ArtifactSpecs specs = task.artifactSpecs
      def artifactSpecs = [
          ArtifactSpec.builder()
                      .artifactNotation('*:*:*:' + appClassifier + '@*')
                      .configuration('nebula')
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
      ]

      artifactSpecs.each { ArtifactSpec advancedSpec ->
        if (!specs.contains(advancedSpec)) {
          task.artifactSpecs.add(advancedSpec)
        } else {
          specs.findAll { it == advancedSpec }.each { it.properties.putAll(advancedSpec.properties) }
        }
      }
    }

  }

}
