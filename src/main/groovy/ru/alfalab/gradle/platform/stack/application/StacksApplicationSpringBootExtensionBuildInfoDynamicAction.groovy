package ru.alfalab.gradle.platform.stack.application

import org.gradle.api.Action

/**
 * @author tolkv
 * @version 20/12/2017
 */
class StacksApplicationSpringBootExtensionBuildInfoDynamicAction implements Action {
  private Map<String, String> additionalProperties

  StacksApplicationSpringBootExtensionBuildInfoDynamicAction(Map<String, String> additionalProperties) {
    this.additionalProperties = additionalProperties
  }

  @Override
  void execute(Object o) {
    o.with {
      buildInfo {
        properties {
          additional.putAll(additionalProperties)
        }
      }
    }
  }

}
