package ru.alfalab.gradle.platform.docs.asciidoctor

import nebula.test.PluginProjectSpec
import org.gradle.api.plugins.PluginContainer
import ru.alfalab.gradle.platform.stack.documentation.asciidoctor.StacksAsciidoctorStaticPlugin

/**
 * @author tolkv
 * @version 27/04/2018
 */
class StacksDocumentationStaticAsciidoctorPluginProjectSpec extends PluginProjectSpec {

  def 'should apply to project with asciidoctor'() {
    when:
      project.plugins.with {
        apply getPluginName()
      }

    then:
      project.plugins.hasPlugin 'org.asciidoctor.convert'
      project.plugins.hasPlugin StacksAsciidoctorStaticPlugin
  }

  @Override
  String getPluginName() {
    return 'stacks.doc.asciidoctor'
  }

}
