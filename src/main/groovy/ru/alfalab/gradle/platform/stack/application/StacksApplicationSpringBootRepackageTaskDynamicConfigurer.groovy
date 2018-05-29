package ru.alfalab.gradle.platform.stack.application

import org.gradle.api.Project
import org.gradle.api.Task
import ru.alfalab.gradle.platform.stack.base.StacksExtension

class StacksApplicationSpringBootRepackageTaskDynamicConfigurer {
  private StacksExtension stacksExtension
  private Project         project

  StacksApplicationSpringBootRepackageTaskDynamicConfigurer(
      StacksExtension stacksExtension, Project project) {
    this.project = project
    this.stacksExtension = stacksExtension
  }

  void configureSpringBootRepackageTask(Task task) {
    if(task.properties.containsKey('withJarTask')) {
      task.withJarTask = project.tasks.findByName('jar')
    }
  }

}
