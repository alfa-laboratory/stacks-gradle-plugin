package ru.alfalab.gradle.platform.app

import nebula.test.ProjectSpec

/**
 * @author tolkv
 * @version 22/12/2017
 */
class StacksCodenarcPluginProjectSpec extends ProjectSpec {

  def 'should not configure codenarc plugin if codenarc.xml doest not exist'() {
    when:
      project.apply plugin: 'groovy'
      project.apply plugin: 'stacks.codenarc'

    then:
      noExceptionThrown()

      project.plugins.hasPlugin('groovy')
      !project.plugins.hasPlugin('codenarc')
  }

  def 'should not configure codenarc plugin without groovy plugin'() {
    when:
      project.apply plugin: 'stacks.codenarc'

    then:
      noExceptionThrown()

      !project.plugins.hasPlugin('codenarc')
  }

  def 'should configure codenarc plugin if codenarc.xml is exist'() {
    given:
      def file = file('config/codenarc/codenarc.xml')

    when:
      project.apply plugin: 'groovy'
      project.apply plugin: 'stacks.codenarc'

    then:
      file.exists()
      noExceptionThrown()

      project.plugins.hasPlugin 'groovy'
      project.plugins.hasPlugin 'codenarc'
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
