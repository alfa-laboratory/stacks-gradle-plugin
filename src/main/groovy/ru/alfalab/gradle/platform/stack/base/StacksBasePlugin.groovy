package ru.alfalab.gradle.platform.stack.base

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import org.gradle.api.tasks.StopExecutionException
import ru.alfalab.gradle.platform.stack.StacksContainerInjector

import static ru.alfalab.gradle.platform.stack.base.StacksConvention.STACKS_CONVENTION_NAME

/**
 * @author tolkv
 * @version 21/12/2017
 */
@CompileStatic
@EqualsAndHashCode
class StacksBasePlugin implements Plugin<Project> {
  @Delegate private Project project

  @Override
  void apply(Project project) {
    this.project = project

    checkGradleVersionAndAbort()
    checkProjectGroupAfterEvaluate()

    extensions.create('stacks', StacksExtension, project)
    convention.plugins.put(STACKS_CONVENTION_NAME, new StacksConvention(new StacksContainerInjector(project)))

    pluginManager.with {
      apply BasePlugin
      apply StacksDefaultRepositoryPlugin
      apply 'nebula.contacts'
      apply 'nebula.info'
    }

  }

  private void checkProjectGroupAfterEvaluate() {
    afterEvaluate {
      if (!group) {
        throw new StopExecutionException(
            """
               \tgroup id must be set
               \tadd next to your root project:
               \tgroup = 'ru.alfalab.<projectname>.<service_name>
               \tallprojects { it.group = rootProject.group }
            """.stripMargin().stripIndent())
      }
    }
  }

  private void checkGradleVersionAndAbort() {
    def majorVersion = project.gradle.gradleVersion.split('\\.').first() as Integer
    if (majorVersion < 4) {
      throwIncompatibleGradleVersion(null)
    }
  }

  private static void throwIncompatibleGradleVersion(Throwable e) {
    throw new GradleException('Gradle will be 4+ version. For upgrade wrapper use \n\tgradle wrapper --gradle-version=4.1\ncommand for example', e)
  }
}
