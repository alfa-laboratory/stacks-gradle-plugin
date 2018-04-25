package ru.alfalab.gradle.platform.stack.application

import org.jfrog.gradle.plugin.artifactory.dsl.ArtifactoryPluginConvention
import org.jfrog.gradle.plugin.artifactory.task.ArtifactoryTask
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import static ru.alfalab.gradle.platform.stack.application.ArtifactoryConventionSpringBootDynamicConfigurator.configureArtifactoryConventionForSpringBoot

@RunWith(MockitoJUnitRunner)
class ArtifactoryConventionSpringBootDynamicConfiguratorTest {

  @Mock ArtifactoryPluginConvention convention
  @Mock ArtifactoryTask             task

  @Test
  void 'should filter properties with null and empty values'() {
    def subject = configureArtifactoryConventionForSpringBoot(convention, task)
        .withAppArtifactProperties([
        nullvalue      : null,
        emptyvalue     : '',
        otheremptyvalue: ' ',
        existedValue   : 'existed'
    ])
    assert subject.appArtifactProperties == [existedValue: 'existed']
  }

}
