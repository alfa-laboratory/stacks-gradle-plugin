package ru.alfalab.gradle.platform.stack.api

import org.gradle.api.Project

/**
 * @author tolkv
 * @version 22/12/2017
 */
interface ProjectAware {
  void setProject(Project project)
}
