package ru.alfalab.gradle.platform.docs

import nebula.test.PluginProjectSpec
import ru.alfalab.gradle.platform.stack.documentation.asciidoctor.StacksAsciidoctorDynamicPlugin

/**
 * @author tolkv
 * @version 27/04/2018
 */
class StacksDocumentationPluginProjectSpec extends PluginProjectSpec {

  def 'should apply asciidoctor subplugin to project with asciidoctor'() {
    when:
      project.plugins.apply 'org.asciidoctor.convert'
      project.plugins.apply getPluginName()

    then:
      project.plugins.hasPlugin(StacksAsciidoctorDynamicPlugin)
  }

  def 'should not apply asciidoctor subplugin to project with asciidoctor'() {
    when:
      project.plugins.apply getPluginName()

    then:
      !project.plugins.hasPlugin(StacksAsciidoctorDynamicPlugin)
  }

  @Override
  String getPluginName() {
    return 'stacks.doc'
  }
}
