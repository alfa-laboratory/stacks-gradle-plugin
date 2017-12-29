package ru.alfalab.gradle.platform.stack.api

import org.gradle.api.artifacts.dsl.RepositoryHandler
import ru.alfalab.gradle.platform.stack.base.StacksExtension

/**
 * @author tolkv
 * @version 22/12/2017
 */
interface RepositoryHandlerAware {
  void setRepositoryHandler(RepositoryHandler handler)
}
