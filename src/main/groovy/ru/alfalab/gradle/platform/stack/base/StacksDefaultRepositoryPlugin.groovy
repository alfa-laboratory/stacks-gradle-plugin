package ru.alfalab.gradle.platform.stack.base

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.logging.Logger

/**
 * @author tolkv
 * @version 28/12/2017
 */
@CompileStatic
@EqualsAndHashCode
class StacksDefaultRepositoryPlugin implements Plugin<Project> {
  @Delegate Project project
  @Delegate Logger  logger

  RepositoryHandler repositoryHandler

  void useJcenterAsDefaultRepo() {
    if (noRepoPresented()) {
      warn 'repo list is empty. Set jcenter as default repository'
      repositoryHandler.jcenter()
    }
  }

  boolean noRepoPresented() {
    repositoryHandler.getAsMap().size() == 0
  }

  @Override
  void apply(Project target) {
    this.project = target
    this.logger = target.logger
    this.repositoryHandler = target.repositories

    afterEvaluate { Project p ->
      useJcenterAsDefaultRepo()
    }
  }
}
