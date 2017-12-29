package ru.alfalab.gradle.platform.stack.api

import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.tasks.TaskContainer

/**
 * @author tolkv
 * @version 22/12/2017
 */
interface ExtensionContainerAware {
  void setExtensionContainer(ExtensionContainer extensions)
}
