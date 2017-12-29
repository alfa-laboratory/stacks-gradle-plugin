package ru.alfalab.gradle.platform.stack.api

import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.plugins.PluginManager

/**
 * @author tolkv
 * @version 22/12/2017
 */
interface PluginContainerAware {
  void setPluginContainer(PluginContainer manager)
}
