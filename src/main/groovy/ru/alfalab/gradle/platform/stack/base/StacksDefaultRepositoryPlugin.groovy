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
      info 'repo list is empty. Set jcenter as default repository'

      repositoryHandler.jcenter()
    } else {
      def jcenter = repositoryHandler.findByName("jcenter")
      if (jcenter) {
        repositoryHandler.remove(jcenter)
        repositoryHandler.addLast(jcenter)
      } else {
        repositoryHandler.jcenter()
      }
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

    useJcenterAsDefaultRepo()
    afterEvaluate { Project p ->
      useJcenterAsDefaultRepo()
    }
  }
}
