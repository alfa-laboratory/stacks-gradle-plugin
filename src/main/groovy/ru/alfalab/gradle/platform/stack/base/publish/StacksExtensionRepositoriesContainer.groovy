package ru.alfalab.gradle.platform.stack.base.publish

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider

/**
 * @author tolkv
 * @version 03/09/2017
 */
@Canonical
@CompileStatic
class StacksExtensionRepositoriesContainer {
  private final Property<String> release
  private final Property<String> snapshot

  StacksExtensionRepositoriesContainer(Project project) {
    release = project.objects.property(String)
    snapshot = project.objects.property(String)
    release.set 'releases'
    snapshot.set 'snapshots'
  }

  String getRelease() {
    return release.get()
  }

  String getSnapshot() {
    return snapshot.get()
  }

  Provider<String> getReleaseProvider() {
    return release
  }

  Provider<String> getSnapshotProvider() {
    return snapshot
  }

  void setRelease(String release) {
    this.release.set(release)
  }

  void setSnapshot(String snapshot) {
    this.snapshot.set(snapshot)
  }

  void useLibsRepositories() {
    release.set 'libs-release-local'
    snapshot.set 'libs-snapshot-local'
  }

  void usePluginsRepositories() {
    release.set 'plugins-release-local'
    snapshot.set 'plugins-snapshot-local'
  }

  void release(String release) {
    this.release.set release
  }

  void snapshot(String snapshot) {
    this.snapshot.set snapshot
  }
}
