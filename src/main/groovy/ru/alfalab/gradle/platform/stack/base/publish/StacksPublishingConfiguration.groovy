package ru.alfalab.gradle.platform.stack.base.publish

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
  private Set<String>                          publicationsForExport
  private Set<String>                          archivesForExport
  private Map<String, String>                  publicationsProperties

  StacksPublishingConfiguration(Project project) {
    this.project = project
    this.repos = new StacksExtensionRepositoriesContainer(project)
    this.publicationsForExport = ['nebula']
    this.archivesForExport = ['archives']
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
   * }</code></pre>
   *
   * <pre><code>
   * repositories {
   *   useLibsRepositories()
   * }</code></pre>
   *
   * <pre><code>
   * repositories {
   *   usePluginsRepositories()
   * }</code></pre>
   * @param config #RepositoriesContainer
   */
  void repositories(@DelegatesTo(StacksExtensionRepositoriesContainer) final Closure config) {
    repos.with config
  }

  /**
   * Add publications for export to artifactory
   * {@code publications 'nebula', 'mubuplication' }
   * @param publicationNames publication names var arg
   */
  void publications(String... publicationNames) {
    this.publicationsForExport.addAll(publicationNames)
  }

  void clearPublications() {
    this.publicationsForExport.clear()
  }

  /**
   * Add publications for export to artifactory
   * {@code publications 'nebula', 'mubuplication' }
   * @param publicationNames publication names var arg
   */
  void archives(String... names) {
    this.archivesForExport.addAll(names)
  }

  void clearArchives() {
    this.archivesForExport.clear()
  }

  StacksExtensionRepositoriesContainer getRepositories() {
    return repos
  }

  /**
   * Set artifactory publication properties
   *
   * <b>for example:</b>
   *
   * <pre><code>
   * publicationsProperties = [
   *   property1: 'value1',
   *   property.two: 'value2'
   * ]</code></pre>
   *
   * @param publicationsProperties
   */
  void setPublicationsProperties(Map<String, String> publicationsProperties) {
    this.publicationsProperties = publicationsProperties
  }

  Map<String, String> getPublicationsProperties() {
    return publicationsProperties
  }
}
