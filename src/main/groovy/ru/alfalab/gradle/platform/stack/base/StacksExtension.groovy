package ru.alfalab.gradle.platform.stack.base

import groovy.transform.Canonical
import org.gradle.api.Project
import ru.alfalab.gradle.platform.stack.application.StacksApplicationConfiguration;
import ru.alfalab.gradle.platform.stack.base.publish.StacksPublishingConfiguration

/**
 * @author tolkv
 * @version 27/12/2017
 */
@Canonical
class StacksExtension {
  private Project                        project
  private StacksPublishingConfiguration  publishingConfig
  private StacksApplicationConfiguration applicationConfig

  StacksExtension(Project p) {
    this.project = p
    this.publishingConfig = new StacksPublishingConfiguration(p)
    this.applicationConfig = new StacksApplicationConfiguration(p)
  }

  void publishing(@DelegatesTo(StacksPublishingConfiguration) final Closure config) {
    this.publishingConfig.with config
  }

  void application(@DelegatesTo(StacksApplicationConfiguration) final Closure config) {
    this.applicationConfig.with config
  }

  StacksPublishingConfiguration getPublishing() {
    return publishingConfig
  }

  StacksApplicationConfiguration getApplicationConfig() {
    return applicationConfig
  }
}
