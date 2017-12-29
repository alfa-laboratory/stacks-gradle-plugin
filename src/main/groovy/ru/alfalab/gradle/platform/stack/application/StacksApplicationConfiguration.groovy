package ru.alfalab.gradle.platform.stack.application

import groovy.transform.Canonical
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider

/**
 * @author tolkv
 * @version 20/12/2017
 */
class StacksApplicationConfiguration {

  StacksApplicationConfiguration(Project project) {
    classifier = project.objects.property(String)
    title = project.objects.property(String)
  }

  /**
   * final. Because only one way to add custom properties to multiple artifacts in a single e
   *
   */
  private Property<String> classifier
  private Property<String> title

  void setClassifier(String classifier) {
    this.@classifier.set(classifier)
  }

  Provider<String> getClassifierProvider() {
    return classifier
  }

  void setTitle(String title) {
    this.@title.set(title)
  }

  Provider<String> getTitleProvider() {
    return title
  }
}
