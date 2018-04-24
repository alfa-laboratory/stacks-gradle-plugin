package ru.alfalab.gradle.platform.stack.base

import groovy.transform.CompileStatic
import nebula.plugin.publishing.publications.JavadocJarPlugin
import nebula.plugin.publishing.publications.SourceJarPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.publish.maven.internal.publication.DefaultMavenPublication
import org.gradle.api.tasks.compile.GroovyCompile
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.api.tasks.testing.Test
import org.jfrog.gradle.plugin.artifactory.ArtifactoryPlugin
import org.jfrog.gradle.plugin.artifactory.task.ArtifactoryTask
import ru.alfalab.gradle.platform.stack.base.publish.StacksPublicationPlugin

/**
 * @author tolkv
 * @version 23/01/2018
 */
//@CompileStatic
class StacksGenericProjectPlugin extends StacksAbstractPlugin {
  @Override
  void applyPlugin() {
    // Publishing
    plugins.withType(JavaPlugin) {
      plugins.apply StacksPublicationPlugin
      plugins.apply JavadocJarPlugin
      plugins.apply SourceJarPlugin
    }

    project.plugins.apply 'nebula.optional-base'
    project.plugins.apply 'nebula.integtest'

    // Info
    project.plugins.apply StacksProjectInformationPlugin

    // Contacts
    project.plugins.apply 'nebula.contacts'

    // Dependency Locking
    project.plugins.apply 'nebula.dependency-lock'
    project.plugins.apply 'nebula.dependency-recommender'

    // TODO Publish javadoc somehow
    project.tasks.withType(Javadoc) { Javadoc task ->
      task.with {
        failOnError = false
        options.encoding 'UTF-8'
      }
    }

    project.tasks.withType(GroovyCompile) { GroovyCompile t ->
      t.with {
        options.encoding = 'UTF-8'
      }
    }

    project.tasks.withType(JavaCompile) { JavaCompile t ->
      t.with {
        options.encoding = 'UTF-8'
        //For map struct and lombok
        if (!options.compilerArgs.contains('-Amapstruct.defaultComponentModel=spring')) {
          options.compilerArgs += '-Amapstruct.defaultComponentModel=spring'
        }
        if (!options.compilerArgs.contains('-Amapstruct.unmappedTargetPolicy=IGNORE')) {
          options.compilerArgs += '-Amapstruct.unmappedTargetPolicy=IGNORE'
        }
      }
    }

    project.tasks.withType(Test) { Test testTask ->
      testTask.testLogging.exceptionFormat = 'full'
    }

    Map<String, String> props = ['platform.fuck': 'fuck', 'platform.label':'app']
    Map<String, String> props2 = ['platform.fuck2': 'fuck2']

    plugins.withType(ArtifactoryPlugin) { ArtifactoryPlugin artifactoryPlugin ->
      plugins.withType(JavadocJarPlugin) {
        tasks.withType(ArtifactoryTask) { ArtifactoryTask t ->
          t.dependsOn tasks.findByName('javadocJar')
          t.skip = false
          project.publishing {
            publications {
              t.publications(nebula)
            }
          }
//          t.properties {
//            nebula props, '*:*:*:' + 'app' + '@*'
//            nebula props, '*:*:*:' + 'groovydoc' + '@*'
//            nebula props, '*:*:*:' + 'javadoc' + '@*'
//          }
        }
      }
    }

    plugins.withType(ArtifactoryPlugin) {
      plugins.withType(SourceJarPlugin) {
        tasks.withType(ArtifactoryTask) { ArtifactoryTask t ->
          t.dependsOn tasks.findByName('sourceJar')
          t.skip = false
          project.publishing {
            publications {
              t.publications(nebula)
            }
          }
//          t.properties {
//            nebula props2, '*:*:*:' + 'sdf' + '@*'
//            nebula props2, '*:*:*:' + 'groovydoc' + '@*'
//            nebula props2, '*:*:*:' + 'javadoc' + '@*'
//          }
        }
      }
    }

  }
}
