package ru.alfalab.gradle.platform.app

import nebula.test.PluginProjectSpec
import ru.alfalab.gradle.platform.stack.application.StacksApplicationPlugin
import ru.alfalab.gradle.platform.stack.application.StacksSpringBootDependenciesPlugin
import ru.alfalab.gradle.platform.stack.application.StacksSpringCloudDependenciesPlugin
import ru.alfalab.gradle.platform.stack.dependencies.StacksDependenciesPlugin

/**
 * @author tolkv
 * @version 29/12/2017
 */
class StacksApplicationPluginProjectSpec extends PluginProjectSpec {
  def 'should apply all plugins [spec]'() {
    when:
      project.apply plugin: StacksApplicationPlugin

    then:
      project.plugins.hasPlugin(StacksDependenciesPlugin)
      project.plugins.hasPlugin(StacksSpringCloudDependenciesPlugin)

  }

  def 'should apply all plugins to root project [spec]'() {
    given:
      def sub = createSubproject(project, 'sub')
      project.subprojects.add(sub)

    when:
      sub.apply plugin: StacksApplicationPlugin

    then:
      sub.plugins.hasPlugin(StacksDependenciesPlugin)
      sub.plugins.hasPlugin(StacksSpringCloudDependenciesPlugin)

      project.plugins.hasPlugin('nebula.dependency-recommender')

  }


  @Override
  String getPluginName() {
    return 'stacks.app'
  }
}
