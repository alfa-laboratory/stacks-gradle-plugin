package ru.alfalab.gradle.platform.stack.application

import groovy.transform.CompileStatic
import nebula.plugin.publishing.publications.JavadocJarPlugin
import nebula.plugin.publishing.publications.SourceJarPlugin
import org.gradle.api.plugins.JavaPlugin
import ru.alfalab.gradle.platform.stack.base.StacksAbstractPlugin
import ru.alfalab.gradle.platform.stack.base.StacksProgramLanguageGroovyPlugin
import ru.alfalab.gradle.platform.stack.base.StacksProgramLanguageGroovydocPlugin
import ru.alfalab.gradle.platform.stack.base.StacksProgramLanguageGroovydocPublishingPlugin
import ru.alfalab.gradle.platform.stack.base.StacksProjectInformationPlugin
import ru.alfalab.gradle.platform.stack.dependencies.StacksDependenciesPlugin

/**
 * @author tolkv
 * @version 20/12/2017
 */
@CompileStatic
class StacksApplicationPlugin extends StacksAbstractPlugin {

  @Override
  void applyPlugin() {
    pluginManager.with {
      apply StacksProgramLanguageGroovyPlugin
      apply 'groovy'

      apply StacksProgramLanguageGroovydocPlugin
      apply StacksProgramLanguageGroovydocPublishingPlugin

      plugins.withType(JavaPlugin) {
        plugins.apply JavadocJarPlugin
        plugins.apply SourceJarPlugin
      }

      project.plugins.apply 'nebula.optional-base'
      project.plugins.apply 'nebula.integtest'

      // Info
      project.plugins.apply StacksProjectInformationPlugin

      // Contacts
      project.plugins.apply 'nebula.contacts'

      // Dependency plugins
      project.plugins.apply 'nebula.dependency-lock'
      project.plugins.apply 'nebula.dependency-recommender'

      if (!rootProject.plugins.hasPlugin('nebula.dependency-recommender')) {
        rootProject.plugins.apply 'nebula.dependency-recommender'
      }

      apply 'org.springframework.boot'
      apply StacksApplicationSpringBootTaskPlugin
      apply StacksApplicationSpringBootPublishingPlugin
      apply StacksDependenciesPlugin
    }
  }

}
