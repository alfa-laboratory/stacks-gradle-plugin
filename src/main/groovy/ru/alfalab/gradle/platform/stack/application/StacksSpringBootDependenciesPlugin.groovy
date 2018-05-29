package ru.alfalab.gradle.platform.stack.application

import groovy.transform.CompileStatic
import org.gradle.api.Plugin
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.dsl.DependencyHandler
import ru.alfalab.gradle.platform.stack.base.StacksAbstractPlugin

/**
 *
 * @author tolkv
 * @version 20/12/2017
 */
@CompileStatic
class StacksSpringBootDependenciesPlugin extends StacksAbstractPlugin {
  public static final String STACKS_DEPENDENCIES_BOOT_DEFAULT = 'stacksDependenciesBootDefault'

  @Override
  void applyPlugin() {
    plugins.apply 'io.spring.dependency-management'

    project.plugins.withId('org.springframework.boot') { Plugin plugin ->
      project.plugins.withId('io.spring.dependency-management') {
        dependencies { DependencyHandler dh ->
          dh.add(resolveDefaultConfiguration(), 'org.springframework.boot:spring-boot-autoconfigure')
        }
      }

    }
  }

  String resolveDefaultConfiguration() {
    Configuration newConf = configurations.findByName('implementation') //for gradle 4.7+

    if (newConf != null) {
      return 'implementation'
    }

    return 'compile' //before gradle 4.7
  }

}
