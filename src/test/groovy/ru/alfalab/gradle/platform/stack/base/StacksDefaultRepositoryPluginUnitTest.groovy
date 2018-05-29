package ru.alfalab.gradle.platform.stack.base

import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.ArtifactRepository
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.logging.Logger
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import static org.mockito.Matchers.anyString
import static org.mockito.Mockito.*

/**
 * @author tolkv
 * @version 28/12/2017
 */
@RunWith(MockitoJUnitRunner)
class StacksDefaultRepositoryPluginUnitTest {
  @Mock        RepositoryHandler             repositoryHandler
  @Mock        Logger                        logger
  @Mock        MavenArtifactRepository       mavenCentral
  @Mock        MavenArtifactRepository       sthRepo
  @Mock        MavenArtifactRepository       jcenter
  @InjectMocks StacksDefaultRepositoryPlugin subject

  @Before
  void setUp() throws Exception {
    when(repositoryHandler.getAsMap()).thenReturn new TreeMap<String, ArtifactRepository>()
  }

  @Test
  void 'should set jcenter if repo was not presented'() {
    subject.useJcenterAsDefaultRepo()

    verify(repositoryHandler, times(1)).jcenter()
  }

  @Test
  void 'should add jcenter as a low priority repository'() {
    given:
      when(repositoryHandler.getAsMap()).thenReturn([
                                                        "mavenCentral": mavenCentral,
                                                        "sthRepo"     : sthRepo,
                                                        "jcenter"     : jcenter
                                                    ] as TreeMap<String, ArtifactRepository>)
      when(repositoryHandler.findByName(anyString())).thenReturn jcenter

      subject.useJcenterAsDefaultRepo()

      verify(repositoryHandler, times(1)).findByName("jcenter")
      verify(repositoryHandler, times(1)).remove(jcenter)
      verify(repositoryHandler, times(1)).addLast(jcenter)
  }


  @Test
  void 'should add jcenter as a low priority repository without jcenter'() {
    given:
      when(repositoryHandler.getAsMap()).thenReturn([
                                                        "mavenCentral": mavenCentral,
                                                        "sthRepo"     : sthRepo,
                                                        "jcenter"     : jcenter
                                                    ] as TreeMap<String, ArtifactRepository>)
      when(repositoryHandler.findByName(anyString())).thenReturn null

      subject.useJcenterAsDefaultRepo()

      verify(repositoryHandler, times(1)).jcenter()
  }
}
