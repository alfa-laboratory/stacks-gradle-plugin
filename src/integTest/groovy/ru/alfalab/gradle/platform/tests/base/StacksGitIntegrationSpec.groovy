package ru.alfalab.gradle.platform.tests.base

import nebula.test.IntegrationSpec
import org.ajoberstar.grgit.Grgit

import java.nio.file.Files

/**
 * @author tolkv
 * @version 29/12/2017
 */
abstract class StacksGitIntegrationSpec extends StacksSimpleIntegrationSpec {
  protected Grgit git
  protected Grgit originGit

  def setup() {
    def origin = new File(projectDir.parent, "${projectDir.name}.git")
    if (origin.exists()) {
      origin.deleteDir()
    }
    origin.mkdirs()

    ['build.gradle', 'settings.gradle'].each {
      Files.move(new File(projectDir, it).toPath(), new File(origin, it).toPath())
    }

    originGit = Grgit.init(dir: origin)
    originGit.add(patterns: ['build.gradle', 'settings.gradle', '.gitignore'] as Set)
    originGit.commit(message: 'Initial checkout')

    git = Grgit.clone(dir: projectDir, uri: origin.absolutePath) as Grgit

    new File(projectDir, '.gitignore') << '''
            .gradle-test-kit
            .gradle
            build/
        '''.stripIndent()

    setupProject()

    git.commit(message: 'Setup')
    git.push()
  }

  abstract void setupProject()


  def cleanup() {
    if (git) git.close()
    if (originGit) originGit.close()
  }
}

