package ru.alfalab.gradle.platform.stack.base

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import org.gradle.api.Project
import ru.alfalab.gradle.platform.stack.application.StacksApplicationConfiguration
import ru.alfalab.gradle.platform.stack.base.publish.StacksPublishingConfiguration
import ru.alfalab.gradle.platform.stack.spring.StacksSpringConfiguration

/**
 * @author tolkv
 * @version 27/12/2017
 */
@Canonical
@CompileStatic
class StacksExtension {
  private Project                        project
  private StacksPublishingConfiguration  publishingConfig
  private StacksApplicationConfiguration applicationConfig
  private StacksSpringConfiguration      springConfig

  StacksExtension(Project p) {
    this.project = p
    this.publishingConfig = new StacksPublishingConfiguration(p)
    this.applicationConfig = new StacksApplicationConfiguration(p)
    this.springConfig = new StacksSpringConfiguration(p)
  }

  void publishing(@DelegatesTo(StacksPublishingConfiguration) final Closure config) {
    this.publishingConfig.with config
  }

  void application(@DelegatesTo(StacksApplicationConfiguration) final Closure config) {
    this.applicationConfig.with config
  }

  void spring(@DelegatesTo(StacksSpringConfiguration) final Closure config) {
    this.springConfig.with config
  }

  StacksPublishingConfiguration getPublishing() {
    return publishingConfig
  }

  StacksApplicationConfiguration getApplicationConfig() {
    return applicationConfig
  }

  StacksSpringConfiguration getSpringConfig() {
    return springConfig
  }

  StacksPublishingConfiguration getPublishingConfig() {
    return publishingConfig
  }
}
