package ru.alfalab.gradle.platform.stack.base

import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.ArtifactRepository
import org.gradle.api.logging.Logger
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import static org.mockito.Mockito.times
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

/**
 * @author tolkv
 * @version 28/12/2017
 */
@RunWith(MockitoJUnitRunner)
class StacksDefaultRepositoryPluginUnitTest {
  @Mock        RepositoryHandler             repositoryHandler
  @Mock        Logger                        logger
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
}
