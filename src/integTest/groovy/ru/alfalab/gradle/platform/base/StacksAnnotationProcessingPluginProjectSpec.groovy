package ru.alfalab.gradle.platform.base

import nebula.test.PluginProjectSpec
import org.gradle.api.tasks.compile.JavaCompile

/**
 * @author tolkv
 * @version 23/01/2018
 */
class StacksAnnotationProcessingPluginProjectSpec extends PluginProjectSpec {

  def 'should apply all plugins [spec]'() {
    when:
      project.apply plugin: 'java'
      project.apply plugin: 'stacks.apt'

    then:
      project.tasks.withType(JavaCompile).every {
        it.options.compilerArgs == [
            '-Amapstruct.defaultComponentModel=spring',
            '-Amapstruct.unmappedTargetPolicy=IGNORE'
        ]
      }
  }

  @Override
  String getPluginName() {
    return 'stacks.apt'
  }

}
