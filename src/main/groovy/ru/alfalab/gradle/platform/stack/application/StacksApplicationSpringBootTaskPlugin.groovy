package ru.alfalab.gradle.platform.stack.application

import groovy.transform.CompileStatic
import nebula.plugin.info.InfoBrokerPlugin
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.tasks.TaskContainer
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

      withSpringBootVersionResolver {

        withSpringBoot1XXAction { Project p ->
          configureClassifierAfterProjectEvaluation 'bootRepackage', stacksExtension
          configureJarTask { Jar j ->
            j.classifier = stacksExtension.applicationConfig.classifierProvider.getOrElse('app')
            afterEvaluate {
              j.classifier = stacksExtension.applicationConfig.classifierProvider.getOrElse('app')
            }
            configureRepackageTask()
          }
        }

        withSpringBoot2XXAction { Project p ->
          configureClassifierAfterProjectEvaluation 'bootJar', stacksExtension
          configureJarTask { Jar j -> j.enabled = false } // not used in SP2 plugin. Disable for avoid conflicts
        }
      }

      //Integrate with nebula.info plugin
      plugins.withType(InfoBrokerPlugin) { InfoBrokerPlugin brokerPlugin ->
        debug 'nebula.info plugin enabled. Try to configure spring boot plugin with nebula.info'

        def manifest = brokerPlugin.buildNonChangingManifest()

        withSpringBootVersionResolver {

          withSpringBoot1XXAction {
            extensionContainer.configure 'springBoot', new StacksApplicationSpringBootExtensionBuildInfo1XXDynamicAction(manifest)
          }

          withSpringBoot2XXAction {
            extensionContainer.configure 'springBoot', new StacksApplicationSpringBootExtensionBuildInfoDynamicAction(manifest)
          }
        }

      }
    }
  }

  private void configureRepackageTask() {
    StacksExtension stacksExtension = extensionContainer.findByType(StacksExtension)

    new StacksApplicationSpringBootRepackageTaskDynamicConfigurer(stacksExtension, project)
        .configureSpringBootRepackageTask(tasks.findByName('bootRepackage'))
  }

  private void configureJarTask(Closure closure) {
    project.tasks.findByName('jar')?.configure closure
  }

  private void withSpringBootVersionResolver(@DelegatesTo(SpringBootVersionsHelper) Closure closure) {
    def helper = new SpringBootVersionsHelper(project)
    helper.with closure
    helper.execute()
  }

  private void configureClassifierAfterProjectEvaluation(String taskName, StacksExtension stacksExtension) {
    def task = project.tasks.findByName(taskName)

    /**
     * Dirty trick for supprot SP1
     * TODO avoid this hack in future version or drop SP1 support
     */
    if (task.class.methods.find { it.name == 'setClassifier' }) {
      new StacksApplicationSpringBootTaskDynamicClassifierConfigurer(stacksExtension, project)
          .configureSpringBootJarClassifier(task)
    } else {
      throw new GradleException("Can't configure classifier for bootJar task or bootRepackage task. Please check plugin and gradle versions.")
    }
  }
}
