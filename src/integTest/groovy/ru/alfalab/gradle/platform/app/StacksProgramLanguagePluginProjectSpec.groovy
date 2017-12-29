package ru.alfalab.gradle.platform.app

import nebula.test.ProjectSpec
import org.gradle.api.tasks.javadoc.Groovydoc
import ru.alfalab.gradle.platform.tests.base.StacksSimpleIntegrationSpec

/**
 * @author tolkv
 * @version 22/12/2017
 */
class StacksProgramLanguagePluginProjectSpec extends ProjectSpec {

  def 'should not configure codenarc plugin if codenarc.xml doest not exist'() {
    when:
      project.apply plugin: 'stacks.lang'

    then:
      noExceptionThrown()

      project.plugins.hasPlugin('groovy')
      !project.plugins.hasPlugin('codenarc')
  }

  def 'should configure codenarc plugin if codenarc.xml is exist'() {
    given:
      def file = file('config/codenarc/codenarc.xml')

    when:
      project.apply plugin: 'stacks.lang'

    then:
      file.exists()
      noExceptionThrown()

      project.plugins.hasPlugin 'groovy'
      project.plugins.hasPlugin 'codenarc'
      project.plugins.hasPlugin 'nebula.javadoc-jar'
      project.plugins.hasPlugin 'nebula.source-jar'
      project.plugins.hasPlugin 'nebula.optional-base'
      project.plugins.hasPlugin 'nebula.dependency-lock'
      project.plugins.hasPlugin 'nebula.dependency-recommender'
      project.plugins.hasPlugin 'nebula.integtest'
  }

  File file(String path, File baseDir = getProjectDir()) {
    def splitted = path.split('/')
    def directory = splitted.size() > 1 ? directory(splitted[0..-2].join('/'), baseDir) : baseDir
    def file = new File(directory, splitted[-1])
    file.createNewFile()
    file
  }

  protected File directory(String path, File baseDir = getProjectDir()) {
    new File(baseDir, path).with {
      mkdirs()
      it
    }
  }
}
