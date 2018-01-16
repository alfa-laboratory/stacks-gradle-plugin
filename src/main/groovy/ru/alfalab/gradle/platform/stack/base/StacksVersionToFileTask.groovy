package ru.alfalab.gradle.platform.stack.base

import groovy.transform.CompileStatic
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

import javax.inject.Inject

/**
 * @author tolkv
 * @version 15/01/2018
 */
@CompileStatic
class StacksVersionToFileTask extends DefaultTask {
  private File projectVersionFile

  StacksVersionToFileTask() {
    projectVersionFile = new File(project.buildDir, 'project-version')
    group = 'ci'
    description = '''
      Add project-version file with project.version content in build directory
      Help to determine project version after build
      '''.stripIndent().stripMargin()
    group = 'stacks-ci'
  }

  @OutputFile
  File getProjectVersionFile() {
    this.projectVersionFile
  }

  @Input
  String getProjectVersion() {
    project.version?.toString()
  }

  @TaskAction
  void writeVersionToFile() {
    if (getProjectVersionFile()) {
      getProjectVersionFile().text = getProjectVersion()
    } else {
      logger.quiet 'project version file not found'
    }
  }

}
