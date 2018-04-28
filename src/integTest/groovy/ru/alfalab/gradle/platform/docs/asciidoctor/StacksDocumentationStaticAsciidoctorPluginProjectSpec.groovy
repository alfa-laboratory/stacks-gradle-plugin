package ru.alfalab.gradle.platform.docs.asciidoctor

import nebula.test.PluginProjectSpec
import ru.alfalab.gradle.platform.stack.documentation.asciidoctor.StacksAsciidoctorDynamicPlugin
import ru.alfalab.gradle.platform.stack.documentation.asciidoctor.StacksAsciidoctorPublishPlugin
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
      project.plugins.hasPlugin StacksAsciidoctorDynamicPlugin
      project.plugins.hasPlugin StacksAsciidoctorPublishPlugin
  }

  def 'should configure project with artifactory'() {
    when:
      project.plugins.with {
        apply getPluginName()
        apply 'com.jfrog.artifactory'
      }

    then:
      project.plugins.hasPlugin 'org.asciidoctor.convert'
      project.plugins.hasPlugin 'com.github.jruby-gradle.base'

      project.plugins.hasPlugin StacksAsciidoctorStaticPlugin
      project.plugins.hasPlugin StacksAsciidoctorDynamicPlugin
      project.plugins.hasPlugin StacksAsciidoctorPublishPlugin

      project.jruby.defaultRepositories == false


      project.asciidoctor.dependsOn.size() == 0

  }

  def 'should configure project with artifactory and java'() {
    when:
      project.plugins.with {
        apply getPluginName()
        apply 'java'
        apply 'com.jfrog.artifactory'
      }

    then:
      project.plugins.hasPlugin 'org.asciidoctor.convert'
      project.plugins.hasPlugin 'com.github.jruby-gradle.base'

      project.plugins.hasPlugin StacksAsciidoctorStaticPlugin
      project.plugins.hasPlugin StacksAsciidoctorDynamicPlugin
      project.plugins.hasPlugin StacksAsciidoctorPublishPlugin

      project.jruby.defaultRepositories == false



      project.asciidoctor.dependsOn.findAll { it.name == 'test' }.size() == 1
  }
  @Override
  String getPluginName() {
    return 'stacks.doc.asciidoctor'
  }

}
