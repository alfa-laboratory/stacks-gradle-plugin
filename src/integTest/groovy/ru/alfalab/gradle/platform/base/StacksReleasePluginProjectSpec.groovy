package ru.alfalab.gradle.platform.base

import nebula.test.PluginProjectSpec
import org.gradle.api.plugins.JavaPlugin
import ru.alfalab.gradle.platform.stack.base.StacksReleasePlugin
import ru.alfalab.gradle.platform.stack.base.publish.StacksArtifactoryPlugin

/**
 * @author tolkv
 * @version 29/12/2017
 */
class StacksReleasePluginProjectSpec extends PluginProjectSpec {

  @Override
  String getPluginName() {
    return 'stacks.release'
  }

}
