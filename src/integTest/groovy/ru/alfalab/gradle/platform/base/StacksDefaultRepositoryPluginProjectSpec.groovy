package ru.alfalab.gradle.platform.base

import nebula.test.PluginProjectSpec

class StacksDefaultRepositoryPluginProjectSpec extends PluginProjectSpec {

  @Override
  String getPluginName() {
    return 'stacks.repositories.default-jcenter'
  }
}
