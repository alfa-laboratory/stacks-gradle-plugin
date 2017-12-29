package ru.alfalab.gradle.platform.stack.base.publish

import groovy.transform.Canonical
import org.gradle.api.Project

/**
 * @author tolkv
 * @version 27/12/2017
 */
class StacksPublishingConfiguration {
  /**
   * Default repositories for publish artifacts
   */
  private Project                              project
  private StacksExtensionRepositoriesContainer repos

  StacksPublishingConfiguration(Project project) {
    this.project = project
    this.repos = new StacksExtensionRepositoriesContainer(project)
  }

  /**
   * Configure repositories
   * Default values:
   * {@code snapshot = 'snapshots'}
   * {@code release = 'releases'}
   *
   * <b>for example:</b>
   *
   * <pre><code>
   * repositories {
   *   snapshot 'my-snapshot-repo'
   *   releases 'my-snapshot-repo'
   * }</pre></code>
   *
   * <pre><code>
   * repositories {
   *   useLibsRepositories()
   * }</pre></code>
   *
   * <pre><code>
   * repositories {
   *   usePluginsRepositories()
   * }</pre></code>
   * @param config #RepositoriesContainer
   */
  void repositories(@DelegatesTo(StacksExtensionRepositoriesContainer) final Closure config) {
    repos.with config
  }

  StacksExtensionRepositoriesContainer getRepositories() {
    return repos
  }
}
