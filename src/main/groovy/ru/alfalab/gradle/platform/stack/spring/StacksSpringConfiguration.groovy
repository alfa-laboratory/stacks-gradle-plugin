package ru.alfalab.gradle.platform.stack.spring

import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider

/**
 * @author tolkv
 * @version 09/01/2018
 */
class StacksSpringConfiguration {
  public final static String DEFAULT_SPRING_BOOT_VERSION = '1.5.9.RELEASE'

  private Property<String> bootVersion

  void setBootVersion(String version) {
    bootVersion.set(version)
  }

  void bootVersion(String version) {
    bootVersion.set(version)
  }

  Provider<String> getBootVersionProvider() {
    return bootVersion
  }

  void setBootVersionProperty(Provider<String> bootVersionProvider) {
    bootVersion.set(bootVersionProvider)
  }

  StacksSpringConfiguration(Project project) {
    bootVersion = project.objects.property(String)
  }

}
