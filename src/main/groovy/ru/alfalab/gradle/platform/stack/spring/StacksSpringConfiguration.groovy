package ru.alfalab.gradle.platform.stack.spring

import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider

/**
 * @author tolkv
 * @version 09/01/2018
 */
class StacksSpringConfiguration {
  public final static String DEFAULT_SPRING_BOOT_VERSION  = '1.5.9.RELEASE'
  public final static String DEFAULT_SPRING_CLOUD_VERSION = 'Edgware.SR1'

  private Property<String> bootVersion
  private Property<String> cloudVersion

  private Property<Boolean> springCloudConfigEnabled
  private Property<Boolean> springCloudBusEnabled
  private Property<Boolean> springCloudSleuthEnabled
  private Property<Boolean> springCloudStreamSleuthEnabled
  private Property<Boolean> springCloudStreamSleuthZipkinEnabled
  private Property<Boolean> springCloudEurekaEnabled
  private Property<Boolean> springCloudFeignEnabled
  private Property<Boolean> springBootWebEnabled

  // Boot version accessors
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

  // Cloud version accessors
  void setCloudVersion(String version) {
    bootVersion.set(version)
  }

  void cloudVersion(String version) {
    cloudVersion.set(version)
  }

  Provider<String> getCloudVersionProvider() {
    return cloudVersion
  }

  void setCloudVersionProperty(Provider<String> cloudVersionProvider) {
    cloudVersion.set(bootVersionProvider)
  }

  // enable disable DSL
  void enableSpringCloudConfig() {
    this.@springCloudConfigEnabled.set(true)
  }
  void enableSpringCloudBus() {
    this.@springCloudBusEnabled.set(true)
  }
  void enableSpringCloudSleuth() {
    this.@springCloudSleuthEnabled.set(true)
  }
  void enableSpringCloudStreamSleuth() {
    this.@springCloudStreamSleuthEnabled.set(true)
  }
  void enableSpringCloudStreamSleuthZipkin() {
    this.@springCloudStreamSleuthZipkinEnabled.set(true)
  }
  void enableSpringCloudEureka() {
    this.@springCloudEurekaEnabled.set(true)
  }
  void enableSpringCloudFeign() {
    this.@springCloudFeignEnabled.set(true)
  }
  void enableSpringBootWeb() {
    this.@springBootWebEnabled.set(true)
  }

  void disableSpringCloudConfig() {
    this.@springCloudConfigEnabled.set(false)
  }
  void disableSpringCloudBus() {
    this.@springCloudBusEnabled.set(false)
  }
  void disableSpringCloudSleuth() {
    this.@springCloudSleuthEnabled.set(false)
  }
  void disableSpringCloudStreamSleuth() {
    this.@springCloudStreamSleuthEnabled.set(false)
  }
  void disableSpringCloudStreamSleuthZipkin() {
    this.@springCloudStreamSleuthZipkinEnabled.set(false)
  }
  void disableSpringCloudEureka() {
    this.@springCloudEurekaEnabled.set(false)
  }
  void disableSpringCloudFeign() {
    this.@springCloudFeignEnabled.set(false)
  }
  void disableSpringBootWeb() {
    this.@springBootWebEnabled.set(false)
  }

  void setSpringCloudConfigEnabled(boolean target) {
    this.@springCloudConfigEnabled.set(target)
  }
  void setSpringCloudBusEnabled(boolean target) {
    this.@springCloudBusEnabled.set(target)
  }
  void setSpringCloudSleuthEnabled(boolean target) {
    this.@springCloudSleuthEnabled.set(target)
  }
  void setSpringCloudStreamSleuthEnabled(boolean target) {
    this.@springCloudStreamSleuthEnabled.set(target)
  }
  void setSpringCloudStreamSleuthZipkinEnabled(boolean target) {
    this.@springCloudStreamSleuthZipkinEnabled.set(target)
  }
  void setSpringCloudEurekaEnabled(boolean target) {
    this.@springCloudEurekaEnabled.set(target)
  }
  void setSpringCloudFeignEnabled(boolean target) {
    this.@springCloudFeignEnabled.set(target)
  }
  void setSpringBootWebEnabled(boolean target) {
    this.@springBootWebEnabled.set(target)
  }


  // get provder helper
  Provider<Boolean> getSpringCloudConfigEnabledProvider() {
    return springCloudConfigEnabled
  }
  Provider<Boolean> getSpringCloudBusEnabledProvider() {
    return springCloudBusEnabled
  }
  Provider<Boolean> getSpringCloudSleuthEnabledProvider() {
    return springCloudSleuthEnabled
  }
  Provider<Boolean> getSpringCloudStreamSleuthEnabledProvider() {
    return springCloudStreamSleuthEnabled
  }
  Provider<Boolean> getSpringCloudStreamSleuthZipkinEnabledProvider() {
    return springCloudStreamSleuthZipkinEnabled
  }
  Provider<Boolean> getSpringCloudEurekaEnabledProvider() {
    return springCloudEurekaEnabled
  }
  Provider<Boolean> getSpringCloudFeignEnabledProvider() {
    return springCloudFeignEnabled
  }
  Provider<Boolean> getSpringBootWebEnabledProvider() {
    return springBootWebEnabled
  }

  StacksSpringConfiguration(Project project) {
    bootVersion = project.objects.property(String)
    cloudVersion = project.objects.property(String)

    springCloudConfigEnabled = project.objects.property(Boolean)
    springCloudBusEnabled = project.objects.property(Boolean)
    springCloudSleuthEnabled = project.objects.property(Boolean)
    springCloudStreamSleuthEnabled = project.objects.property(Boolean)
    springCloudStreamSleuthZipkinEnabled = project.objects.property(Boolean)
    springCloudEurekaEnabled = project.objects.property(Boolean)
    springCloudFeignEnabled = project.objects.property(Boolean)
    springBootWebEnabled = project.objects.property(Boolean)
  }
}
