package ru.alfalab.gradle.platform.base

import nebula.plugin.contacts.ContactsPlugin
import nebula.plugin.dependencylock.DependencyLockPlugin
import nebula.plugin.info.InfoPlugin
import nebula.plugin.publishing.publications.JavadocJarPlugin
import nebula.plugin.publishing.publications.SourceJarPlugin
import nebula.test.PluginProjectSpec
import org.gradle.api.tasks.compile.GroovyCompile
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import ru.alfalab.gradle.platform.stack.base.StacksGenericProjectPlugin
import ru.alfalab.gradle.platform.stack.base.publish.StacksPublicationPlugin
import ru.alfalab.gradle.platform.stack.base.StacksReleasePlugin

/**
 * @author tolkv
 * @version 23/01/2018
 */
class StacksGenericProjectPluginProjectSpec extends PluginProjectSpec {

  def 'should apply all plugins [spec]'() {
    when:
      project.apply plugin: StacksGenericProjectPlugin

    then:
      project.plugins.hasPlugin(StacksPublicationPlugin)
      project.plugins.hasPlugin(JavadocJarPlugin)
      project.plugins.hasPlugin(SourceJarPlugin)
      project.plugins.hasPlugin(InfoPlugin)
      project.plugins.hasPlugin(ContactsPlugin)
      project.plugins.hasPlugin(DependencyLockPlugin)
      !project.plugins.hasPlugin(StacksReleasePlugin)

      project.tasks.withType(Javadoc).every { (!it.failOnError) }
      project.tasks.withType(Test).every { it.testLogging.exceptionFormat == TestExceptionFormat.FULL }
      project.tasks.withType(JavaCompile).every { it.options.encoding == 'UTF-8' }
      project.tasks.withType(JavaCompile).every {
        it.options.compilerArgs == [
            '-Amapstruct.defaultComponentModel=spring',
            '-Amapstruct.unmappedTargetPolicy=IGNORE'
        ]
      }
      project.tasks.withType(GroovyCompile).every { it.options.encoding == 'UTF-8' }

  }

  @Override
  String getPluginName() {
    return 'stacks.project.generic'
  }

}
