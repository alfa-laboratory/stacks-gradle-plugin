package ru.alfalab.gradle.platform.stack.base

import groovy.transform.CompileStatic
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger

/**
 * @author tolkv
 * @version 27/12/2017
 */
@CompileStatic
abstract class StacksAbstractPlugin implements Plugin<Project> {
  @Delegate private Logger  logger
  @Delegate private Project project

  static { //fail fast
    try {
      Class<?> name = Class.forName('ru.alfalab.gradle.platform.stack.base.publish.StacksExtensionRepositoriesContainer')
      assert name
    } catch (NoClassDefFoundError e) {
      throwIncompatibleGradleVersion(e)
    }
  }

  @Override
  void apply(Project target) {
    this.logger = target.logger
    this.project = target
    checkGradleVersionAndAbort()

    pluginManager.apply StacksBasePlugin

    StacksConvention stacksConvention = convention.getPlugin(StacksConvention)
    stacksConvention.stacksInjector.injectAll(this)

    applyPlugin()
  }

  abstract void applyPlugin()

  private void checkGradleVersionAndAbort() {
    def majorVersion = project.gradle.gradleVersion.split('\\.').first() as Integer
    if (majorVersion < 4) {
      throwIncompatibleGradleVersion(null)
    }
  }

  private static void throwIncompatibleGradleVersion(Throwable e) {
    throw new GradleException('Gradle will be 4+ version. For upgrade wrapper use \n\tgradle wrapper --gradle-version=4.1\ncommand for example', e)
  }
}
