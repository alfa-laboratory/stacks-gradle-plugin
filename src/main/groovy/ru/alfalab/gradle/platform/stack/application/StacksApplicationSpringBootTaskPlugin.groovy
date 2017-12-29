package ru.alfalab.gradle.platform.stack.application

import groovy.transform.CompileStatic
import nebula.plugin.info.InfoBrokerPlugin
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.tasks.TaskContainer
import org.gradle.jvm.tasks.Jar
import org.springframework.boot.gradle.SpringBootPluginExtension
import org.springframework.boot.gradle.buildinfo.BuildInfo
import org.springframework.boot.gradle.plugin.SpringBootPlugin
import org.springframework.boot.gradle.repackage.RepackageTask
import ru.alfalab.gradle.platform.stack.api.ExtensionContainerAware
import ru.alfalab.gradle.platform.stack.api.PluginContainerAware
import ru.alfalab.gradle.platform.stack.api.TaskContainerAware
import ru.alfalab.gradle.platform.stack.base.StacksAbstractPlugin
import ru.alfalab.gradle.platform.stack.base.StacksExtension
import ru.alfalab.gradle.platform.stack.base.StacksProgramLanguagePlugin

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
    plugins.withType(SpringBootPlugin) { SpringBootPlugin p ->
      debug 'spring boot enabled. Check nebula.info plugin'

      extensionContainer.configure(SpringBootPluginExtension, { SpringBootPluginExtension springExtension ->
        springExtension.buildInfo()
      } as Action<SpringBootPluginExtension>)

      String jarTaskDefaultName = JavaPlugin.JAR_TASK_NAME

      afterEvaluate {
        taskContainer.getByName(jarTaskDefaultName) { Jar j ->
          StacksExtension stacksExtension = extensionContainer.findByType(StacksExtension)
          if (!j.classifier) {
            j.classifier = stacksExtension.applicationConfig.classifierProvider.getOrElse('app')
          }
        }
      }

      pluginContainer.withType(InfoBrokerPlugin) { InfoBrokerPlugin brokerPlugin ->
        debug 'nebula.info plugin enabled. Try to configure spring boot plugin with nebula.info'

        def manifest = brokerPlugin.buildNonChangingManifest()

        taskContainer.withType(BuildInfo) { BuildInfo task ->
          task.additionalProperties.putAll(manifest)
        }

        taskContainer.withType(RepackageTask) { RepackageTask t ->
          t.withJarTask = taskContainer.findByName(jarTaskDefaultName)
        }
      }

    }
  }

}
