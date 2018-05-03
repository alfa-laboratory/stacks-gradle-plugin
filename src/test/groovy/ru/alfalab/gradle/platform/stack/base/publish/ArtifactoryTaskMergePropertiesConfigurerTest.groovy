package ru.alfalab.gradle.platform.stack.base.publish

import groovy.transform.CompileStatic
import org.gradle.api.Action
import org.gradle.api.Task
import org.jfrog.build.extractor.clientConfiguration.ArtifactSpec
import org.jfrog.build.extractor.clientConfiguration.ArtifactSpecs
import org.jfrog.gradle.plugin.artifactory.task.ArtifactoryTask
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.runners.MockitoJUnitRunner

import java.util.Arrays

import static org.junit.Assert.*
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

@CompileStatic
@RunWith(MockitoJUnitRunner.class)
class ArtifactoryTaskMergePropertiesConfigurerTest {
  @Mock        ArtifactoryTask                          artifactoryTask
  @InjectMocks ArtifactoryTaskMergePropertiesConfigurer artifactoryTaskMergePropertiesConfigurer
  @Captor      ArgumentCaptor<Action<Task>>             actionArgumentCaptor

  @Test
  void 'should merge and override specs set'() {
    //given
    when(artifactoryTask.getArtifactSpecs())
        .thenReturn([
                        ArtifactSpec.builder()
                                    .artifactNotation('*:*:*:app@*')
                                    .configuration('nebula')
                                    .properties([type: 'override-app-to-app2'])
                                    .build(),
                        ArtifactSpec.builder()
                                    .artifactNotation('*:*:*:new@*')
                                    .configuration('new')
                                    .properties([type: 'new'])
                                    .build(),
                    ] as ArtifactSpecs)
    //when
    artifactoryTaskMergePropertiesConfigurer.putAllSpecTo([
        ArtifactSpec.builder()
                    .artifactNotation('*:*:*:app@*')
                    .configuration('nebula')
                    .properties([type: 'app'])
                    .build(),
        ArtifactSpec.builder()
                    .artifactNotation('*:*:*:groovydoc@*')
                    .configuration('all')
                    .properties([type: 'groovydoc'])
                    .build(),
        ArtifactSpec.builder()
                    .artifactNotation('*:*:*:javadoc@*')
                    .configuration('nebula')
                    .properties([type: 'javadoc'])
                    .build(),
        ArtifactSpec.builder()
                    .artifactNotation('*:*:*:sources@*')
                    .configuration('nebula')
                    .properties([type: 'sources'])
                    .build()
    ])

    //then
    verify(artifactoryTask).doFirst(actionArgumentCaptor.capture())
    def value = actionArgumentCaptor.getValue()

    //and when
    value.execute(artifactoryTask)

    //and then
    assert artifactoryTask.getArtifactSpecs()
                          .findAll { it.classifier == 'app' }
                          .first()
                          .properties['type'] == 'override-app-to-app2'
    assert artifactoryTask.getArtifactSpecs()
                          .findAll { it.classifier == 'new' }
                          .first()
                          .properties['type'] == 'new'

    assert artifactoryTask.getArtifactSpecs().size() == 5
  }
}