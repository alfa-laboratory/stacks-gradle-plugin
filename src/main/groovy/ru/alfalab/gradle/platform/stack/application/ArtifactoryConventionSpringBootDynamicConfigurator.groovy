package ru.alfalab.gradle.platform.stack.application

import org.jfrog.gradle.plugin.artifactory.dsl.ArtifactoryPluginConvention

class ArtifactoryConventionSpringBootDynamicConfigurator {
  private ArtifactoryPluginConvention artifactoryConvention

  private String              appClassifier = 'app'
  private Map<String, String> appArtifactProperties
  private Map<String, String> groovydocArtifactProperties
  private Map<String, String> javadocArtifactProperties
  private Map<String, String> sourcesArtifactProperties

  ArtifactoryConventionSpringBootDynamicConfigurator(
      ArtifactoryPluginConvention artifactoryConvention) {
    this.artifactoryConvention = artifactoryConvention
    this
  }

  static ArtifactoryConventionSpringBootDynamicConfigurator configureArtifactoryConventionForSpringBoot(
      ArtifactoryPluginConvention convention) {
    return new ArtifactoryConventionSpringBootDynamicConfigurator(convention)
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
          properties {
            nebula appArtifactProperties, '*:*:*:' + appClassifier + '@*'
            nebula groovydocArtifactProperties, '*:*:*:groovydoc@*'
            nebula javadocArtifactProperties, '*:*:*:javadoc@*'
            nebula sourcesArtifactProperties, '*:*:*:sources@*'
          }
        }
      }
    }
  }

}
