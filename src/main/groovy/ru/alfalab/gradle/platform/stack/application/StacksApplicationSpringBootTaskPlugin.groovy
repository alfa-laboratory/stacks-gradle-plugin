package ru.alfalab.gradle.platform.stack.application

import groovy.transform.CompileStatic
import nebula.plugin.info.InfoBrokerPlugin
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.bundling.AbstractArchiveTask
import org.gradle.jvm.tasks.Jar
import ru.alfalab.gradle.platform.stack.api.ExtensionContainerAware
import ru.alfalab.gradle.platform.stack.api.PluginContainerAware
import ru.alfalab.gradle.platform.stack.api.TaskContainerAware
import ru.alfalab.gradle.platform.stack.application.spring.SpringBootVersionsHelper
import ru.alfalab.gradle.platform.stack.base.StacksAbstractPlugin
import ru.alfalab.gradle.platform.stack.base.StacksExtension

/**
 * @author tolkv
 * @version 20/12/2017
 */
@CompileStatic
class StacksApplicationSpringBootTaskPlugin extends StacksAbstractPlugin implements PluginContainerAware, ExtensionContainerAware, TaskContainerAware {
  PluginContainer    pluginContainer
  TaskContainer      taskContainer
  ExtensionContainer extensionContainer

  @Override
  void applyPlugin() {
    plugins.withId('org.springframework.boot') {
      debug 'spring boot enabled. Check nebula.info plugin'

      StacksExtension stacksExtension = extensionContainer.findByType(StacksExtension)

      tasks.findByName('jar')?.configure { Jar j ->
        j.enabled = false
      }

      new SpringBootVersionsHelper(project).withSpringBoot1XXAction {
        Project p -> configureClassifierAfterProjectEvaluation 'bootRepackage', stacksExtension
      }.withSpringBoot2XXAction {
        Project p -> configureClassifierAfterProjectEvaluation 'bootJar', stacksExtension
      }.execute()

      //Integrate with nebula.info plugin
      plugins.withType(InfoBrokerPlugin) { InfoBrokerPlugin brokerPlugin ->
        debug 'nebula.info plugin enabled. Try to configure spring boot plugin with nebula.info'

        def manifest = brokerPlugin.buildNonChangingManifest()

        extensionContainer.configure 'springBoot', new StacksApplicationSpringBootExtensionBuildInfoDynamicAction(manifest)
      }
    }
  }

  private void configureClassifierAfterProjectEvaluation(String taskName, StacksExtension stacksExtension) {
    def task = project.tasks.findByName(taskName)
    if(task instanceof AbstractArchiveTask) {
      //WTF ? Two times! Need to investigate this mistake..
      //without afterEvaluate ru.alfalab.gradle.platform.app.StacksApplicationPluginIntegSpec does not working
      //without simple set ru.alfalab.gradle.platform.app.StacksApplicationPluginPublishingIntegSpec does not working
      //both required :(
      def archiveTask = task as AbstractArchiveTask
      archiveTask.classifier = extractClassifier(stacksExtension)
      afterEvaluate {
        archiveTask.classifier = extractClassifier(stacksExtension)
      }
    } else {
      throw new GradleException("Can't configure classifier for bootJar task or bootRepackage task. Please check plugin and gradle versions.")
    }
  }

  private static String extractClassifier(StacksExtension stacksExtension) {
    stacksExtension.applicationConfig.classifierProvider.getOrElse('app')
  }

}
