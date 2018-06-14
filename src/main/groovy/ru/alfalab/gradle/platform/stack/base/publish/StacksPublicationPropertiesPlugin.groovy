package ru.alfalab.gradle.platform.stack.base.publish

import groovy.transform.CompileStatic
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.tasks.TaskContainer
import org.jfrog.gradle.plugin.artifactory.ArtifactoryPlugin
import ru.alfalab.gradle.platform.stack.api.ExtensionContainerAware
import ru.alfalab.gradle.platform.stack.api.TaskContainerAware
import ru.alfalab.gradle.platform.stack.base.StacksAbstractPlugin
import ru.alfalab.gradle.platform.stack.base.StacksExtension

/**
 * @author tolkv
 * @version 27/12/2017
 */
@CompileStatic
class StacksPublicationPropertiesPlugin
  extends StacksAbstractPlugin
  implements TaskContainerAware, ExtensionContainerAware {


  TaskContainer taskContainer
  ExtensionContainer extensionContainer


  @Override
  void applyPlugin() {
    plugins.withType(ArtifactoryPlugin) {
      plugins.withType(MavenPublishPlugin) {
        debug 'configure publications properties'

        StacksExtension stacksExtension = extensionContainer.findByType(StacksExtension)

        stacksExtension.publishingConfig.publicationsProperties.each { k, v ->
          debug ":: PROPERTY $k -> $v"
        }
      }
    }
  }

}
