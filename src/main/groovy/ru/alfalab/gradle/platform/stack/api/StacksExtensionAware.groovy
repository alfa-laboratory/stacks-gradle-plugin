package ru.alfalab.gradle.platform.stack.api

import org.gradle.api.plugins.ExtensionContainer
import ru.alfalab.gradle.platform.stack.base.StacksExtension

/**
 * @author tolkv
 * @version 22/12/2017
 */
interface StacksExtensionAware {
  void setStacksExtension(StacksExtension extension)
}
