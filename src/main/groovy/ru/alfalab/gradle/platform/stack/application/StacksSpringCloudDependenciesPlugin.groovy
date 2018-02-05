package ru.alfalab.gradle.platform.stack.application

import groovy.transform.CompileStatic
import io.spring.gradle.dependencymanagement.DependencyManagementPlugin
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import io.spring.gradle.dependencymanagement.dsl.ImportsHandler
import org.gradle.api.Action
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.tasks.TaskContainer
import org.springframework.boot.gradle.plugin.SpringBootPlugin
import ru.alfalab.gradle.platform.stack.api.ExtensionContainerAware
import ru.alfalab.gradle.platform.stack.api.PluginContainerAware
import ru.alfalab.gradle.platform.stack.api.TaskContainerAware
import ru.alfalab.gradle.platform.stack.base.StacksAbstractPlugin
import ru.alfalab.gradle.platform.stack.base.StacksExtension

import static ru.alfalab.gradle.platform.stack.spring.StacksSpringConfiguration.DEFAULT_SPRING_CLOUD_VERSION

/**
 * @author tolkv
 * @version 20/12/2017
 */
@CompileStatic
class StacksSpringCloudDependenciesPlugin extends StacksAbstractPlugin implements PluginContainerAware, ExtensionContainerAware, TaskContainerAware {
  PluginContainer    pluginContainer
  TaskContainer      taskContainer
  ExtensionContainer extensionContainer

  @Override
  void applyPlugin() {
    pluginContainer.withType(SpringBootPlugin) {
      pluginContainer.withType(DependencyManagementPlugin) { DependencyManagementPlugin p ->

        afterEvaluate {

          extensionContainer.configure(DependencyManagementExtension) { DependencyManagementExtension extension ->
            def springConfiguration = extensionContainer.findByType(StacksExtension).springConfig
            def resolvedSpringCloudVersion = springConfiguration.cloudVersionProvider.getOrElse(DEFAULT_SPRING_CLOUD_VERSION)

            extension.imports(
                { ImportsHandler importsHandler ->
                  importsHandler.mavenBom "org.springframework.cloud:spring-cloud-dependencies:${resolvedSpringCloudVersion}"
                } as Action<ImportsHandler>)
          }

        }

      }
    }
  }

}
