package ru.alfalab.gradle.platform.stack.application

import org.gradle.api.Action

/**
 * @author tolkv
 * @version 20/12/2017
 */
class StacksApplicationSpringBootExtensionBuildInfo1XXDynamicAction implements Action {
  private Map<String, String> _additionalProperties

  StacksApplicationSpringBootExtensionBuildInfo1XXDynamicAction(Map<String, String> additionalProperties) {
    this._additionalProperties = additionalProperties
  }

  @Override
  void execute(Object o) {
    o.with {
      buildInfo {
        additionalProperties.putAll(_additionalProperties)
      }
    }
  }

}
