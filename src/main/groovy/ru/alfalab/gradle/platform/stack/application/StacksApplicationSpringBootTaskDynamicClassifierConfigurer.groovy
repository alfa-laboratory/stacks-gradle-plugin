package ru.alfalab.gradle.platform.stack.application

import org.gradle.api.Project
import org.gradle.api.Task
import ru.alfalab.gradle.platform.stack.base.StacksExtension

class StacksApplicationSpringBootTaskDynamicClassifierConfigurer {
  private StacksExtension stacksExtension
  private Project         project

  StacksApplicationSpringBootTaskDynamicClassifierConfigurer(
      StacksExtension stacksExtension, Project project) {
    this.project = project
    this.stacksExtension = stacksExtension
  }

  void configureSpringBootJarClassifier(Task task) {
    //WTF ? Two times! Need to investigate this mistake..
    //without afterEvaluate ru.alfalab.gradle.platform.app.StacksApplicationPluginIntegSpec does not working
    //without simple set ru.alfalab.gradle.platform.app.StacksApplicationPluginPublishingIntegSpec does not working
    //both required :(
    task.classifier = extractClassifier(stacksExtension)
    project.afterEvaluate {
      task.classifier = extractClassifier(stacksExtension)
    }
  }

  private static String extractClassifier(StacksExtension stacksExtension) {
    stacksExtension.applicationConfig.classifierProvider.getOrElse('app')
  }
}
