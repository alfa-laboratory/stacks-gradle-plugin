package ru.alfalab.gradle.platform.stack.publishing

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Test
import ru.alfalab.gradle.platform.stack.base.publish.StacksPublishingConfiguration
import spock.lang.Subject
/**
 * @author tolkv
 * @version 02/09/2017
 */
class StacksExtensionPublishingUnitTest {
  @Subject
  StacksPublishingConfiguration extension
  Project                       project = ProjectBuilder.builder().withName('testproject').build()

  @Before
  void init() {
    extension = new StacksPublishingConfiguration(project)
  }

  @Test
  void 'should set repositories'() {
    given: 'want to customize snapshot and release repo'
    def want_toSetSnapshotRepoName = 'supermega-snapshots'
    def want_toSetReleaseRepoName = 'super-release'

    when: 'apply repositories configuration'
    extension.repositories {
      snapshot = want_toSetSnapshotRepoName
      release = want_toSetReleaseRepoName
    }

    then:
    assert extension.repositories.snapshot == want_toSetSnapshotRepoName
    assert extension.repositories.release == want_toSetReleaseRepoName
  }

  @Test
  void 'should set repositories [works with method too]'() {
    given: 'want to customize snapshot and release repo with advanced DSL'
    def want_toSetSnapshotRepoName = 'supermega-snapshots'
    def want_toSetReleaseRepoName = 'super-release'

    when: 'apply repositories configuration'
    extension.repositories {
      snapshot want_toSetSnapshotRepoName
      release want_toSetReleaseRepoName
    }

    then:
    assert extension.repositories.snapshot == want_toSetSnapshotRepoName
    assert extension.repositories.release == want_toSetReleaseRepoName
  }

  @Test
  void 'should customize only snapshots repo'() {
    given: 'want to customize snapshot repo with advanced DSL'
    def want_toSetSnapshotRepoName = 'supermega-snapshots'

    when: 'apply repositories configuration'
    extension.repositories {
      snapshot want_toSetSnapshotRepoName
    }

    then:
    assert extension.repositories.snapshot == want_toSetSnapshotRepoName
    assert extension.repositories.release == 'releases'
  }

  @Test
  void 'should customize only releases repo'() {
    given: 'want to customize release repo with advanced DSL'
    def want_toSetReleaseRepoName = 'super-release'

    when: 'apply repositories configuration'
    extension.repositories {
      release want_toSetReleaseRepoName
    }

    then:
    assert extension.repositories.snapshot == 'snapshots'
    assert extension.repositories.release == want_toSetReleaseRepoName
  }

  @Test
  void 'should use libs shortcut'() throws Exception {
    when: 'use fast way to set libs repos'
    extension.repositories {
      useLibsRepositories()
    }

    then:
    extension.repositories.with {
      assert snapshot.contains('libs')
      assert snapshot.contains('snapshot')
      assert release.contains('libs')
      assert release.contains('release')
    }
  }

  @Test
  void 'should use plugins repositories shortcut'() throws Exception {
    when: 'use fast way to set plugins local repos'
    extension.repositories {
      usePluginsRepositories()
    }

    then:
    extension.repositories.with {
      assert snapshot.contains('plugins')
      assert snapshot.contains('snapshot')
      assert release.contains('plugins')
      assert release.contains('release')
    }
  }
}
