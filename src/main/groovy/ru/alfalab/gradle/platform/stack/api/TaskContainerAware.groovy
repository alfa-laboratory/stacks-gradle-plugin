package ru.alfalab.gradle.platform.stack.api

import org.gradle.api.Project
import org.gradle.api.tasks.TaskContainer

/**
 * @author tolkv
 * @version 22/12/2017
 */
interface TaskContainerAware {
  void setTaskContainer(TaskContainer tasks)
}
