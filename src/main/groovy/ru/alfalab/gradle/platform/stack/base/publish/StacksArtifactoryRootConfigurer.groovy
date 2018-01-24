package ru.alfalab.gradle.platform.stack.base.publish

import org.jfrog.gradle.plugin.artifactory.dsl.ArtifactoryPluginConvention

/**
 * @author tolkv
 * @version 27/12/2017
 */
class StacksArtifactoryRootConfigurer {
  @Delegate private ArtifactoryPluginConvention   convention
  @Delegate private StacksPublishingConfiguration stacksPublishingExtension

  void configure(ArtifactoryPluginConvention convention, StacksPublishingConfiguration publishing) {
    this.convention = convention
    this.stacksPublishingExtension = publishing

    String resolvedContextUrl = clientConfig.publisher.contextUrl ? clientConfig.publisher.contextUrl : project.findProperty('artifactory_contextUrl')
    String resolvedRepoKey = isDevelopmentVersion() ? repositories.snapshot : repositories.release
    String resolvedUser = clientConfig.publisher.username ? clientConfig.publisher.username : project.findProperty('artifactory_user')
    String resolvedPassword = clientConfig.publisher.password ? clientConfig.publisher.password : project.findProperty('artifactory_password')

    project.logger.info "resolvedContextUrl   : $resolvedContextUrl"
    project.logger.info "resolvedRepoKey      : $resolvedRepoKey"
    project.logger.info "resolvedUser         : $resolvedUser"
    project.logger.info "len(resolvedPassword): ${resolvedPassword?.size()}"

    project.logger.info "platform.deployment.app-name: ${project.name}"
    project.logger.info "version                     : ${project.name}"

    publish {
      contextUrl = resolvedContextUrl
      repository {
        repoKey = resolvedRepoKey
        username = resolvedUser
        password = resolvedPassword
      }

    }
  }

  private boolean isDevelopmentVersion() {
    project.version.toString().contains('SNAPSHOT') || project.version.toString().contains('-dev')
  }
}
