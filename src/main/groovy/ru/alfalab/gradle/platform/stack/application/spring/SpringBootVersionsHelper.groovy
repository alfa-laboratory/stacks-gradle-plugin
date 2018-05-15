package ru.alfalab.gradle.platform.stack.application.spring

import org.gradle.api.Action
import org.gradle.api.GradleException
import org.gradle.api.Project

class SpringBootVersionsHelper {
  final Project targetProject

  private Action<Project> springBoot1XXAction
  private Action<Project> springBoot2XXAction

  SpringBootVersionsHelper(Project targetProject) {
    this.targetProject = targetProject
  }

  SpringBootVersionsHelper withSpringBoot1XXAction(Action<Project> action) {
    springBoot1XXAction = action
    return this
  }

  SpringBootVersionsHelper withSpringBoot2XXAction(Action<Project> action) {
    springBoot2XXAction = action
    return this
  }

  void execute() {
    def boot2XXSign = taskExistSpringBoot2XXSign()
    def boot1XXSign = taskExistSpringBoot1XXSign()

    if(boot1XXSign && boot2XXSign) {
      throw new GradleException("Can't determine Spring Boot version — 1XX or 2XX. I might be because bootJar[2XX] and bootRepackage[1xx] exists in one project")
    }

    if (boot2XXSign && springBoot2XXAction) {
      springBoot2XXAction.execute(targetProject)
    }

    if (boot1XXSign && springBoot1XXAction) {
      springBoot1XXAction.execute(targetProject)
    }
  }

  private boolean taskExistSpringBoot1XXSign() {
    targetProject.tasks.findByName("bootRepackage") != null
  }

  private boolean taskExistSpringBoot2XXSign() {
    targetProject.tasks.findByName('bootJar') != null
  }


}
