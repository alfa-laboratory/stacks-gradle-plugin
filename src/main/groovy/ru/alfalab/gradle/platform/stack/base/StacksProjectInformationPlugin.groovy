package ru.alfalab.gradle.platform.stack.base

import groovy.transform.CompileStatic
import nebula.plugin.info.InfoBrokerPlugin
import nebula.plugin.info.InfoPlugin
import nebula.plugin.info.InfoReporterPlugin
import nebula.plugin.info.scm.ScmInfoPlugin
import org.gradle.api.Action
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.tasks.TaskContainer
import org.jfrog.gradle.plugin.artifactory.ArtifactoryPlugin
import org.jfrog.gradle.plugin.artifactory.task.ArtifactoryTask
import ru.alfalab.gradle.platform.stack.api.PluginContainerAware
import ru.alfalab.gradle.platform.stack.api.TaskContainerAware

/**
 * @author tolkv
 * @version 21/12/2017
 */
@CompileStatic
class StacksProjectInformationPlugin extends StacksAbstractPlugin implements TaskContainerAware, PluginContainerAware, InfoReporterPlugin {
  TaskContainer   taskContainer
  PluginContainer pluginContainer

  @Override
  void applyPlugin() {
    pluginContainer.apply(InfoPlugin)

    addMetaInformationForArtifactoryTask { ArtifactoryTask task ->
      afterEvaluate {

        project.plugins.withType(InfoBrokerPlugin) { InfoBrokerPlugin manifestPlugin ->
          def artifactMetaProperties = manifestPlugin.buildManifest()
          if (task.properties) {
            debug "add properties[$artifactMetaProperties] to task[$task.name]"
            artifactMetaProperties.each {
              if(it.value) {
                //because ; — is a separator for property in artifactory api
                def escapedValue = it.value.replace(';', '-')
                task.properties.put(it.key, escapedValue)
              }
            }
          }
        }

      }

    }
  }

  private void addMetaInformationForArtifactoryTask(Action<ArtifactoryTask> taskClosure) {
    pluginContainer.withType(ScmInfoPlugin) {
      pluginContainer.withType(ArtifactoryPlugin) {
        taskContainer.withType(ArtifactoryTask, taskClosure)
      }
    }
  }

}
