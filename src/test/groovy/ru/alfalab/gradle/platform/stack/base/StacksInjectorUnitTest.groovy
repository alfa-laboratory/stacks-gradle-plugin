package ru.alfalab.gradle.platform.stack.base

import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.logging.Logger
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.tasks.TaskContainer
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import ru.alfalab.gradle.platform.stack.StacksContainerInjector
import ru.alfalab.gradle.platform.stack.api.ExtensionContainerAware
import ru.alfalab.gradle.platform.stack.api.PluginContainerAware
import ru.alfalab.gradle.platform.stack.api.ProjectAware
import ru.alfalab.gradle.platform.stack.api.RepositoryHandlerAware
import ru.alfalab.gradle.platform.stack.api.StacksExtensionAware
import ru.alfalab.gradle.platform.stack.api.TaskContainerAware

import static org.mockito.Mockito.when

/**
 * @author tolkv
 * @version 22/12/2017
 */
@RunWith(MockitoJUnitRunner)
class StacksInjectorUnitTest {
  @Mock Project             project
  @Mock ExtensionContainer  extensionContainer
  @Mock PublishingExtension publishingExtension
  @Mock TaskContainer       taskContainer
  @Mock PluginContainer     pluginContainer
  @Mock StacksExtension     stacksExtension
  @Mock RepositoryHandler   repositoryHandler
  @Mock Logger              logger

  @Before
  void setUp() throws Exception {
    when(project.logger).thenReturn logger

    when(extensionContainer.findByType(PublishingExtension)).thenReturn publishingExtension
    when(extensionContainer.findByType(StacksExtension)).thenReturn stacksExtension

    when(project.extensions).thenReturn extensionContainer
    when(project.tasks).thenReturn taskContainer
    when(project.plugins).thenReturn pluginContainer
    when(project.repositories).thenReturn repositoryHandler
  }

  @Test
  void 'should inject to all Aware interface'() {
    given:
      def injector = new StacksContainerInjector(project)
      def subject = new TestSubject()

    when:
      injector.injectAll(subject)

    then:
      assert subject.extensionContainer == extensionContainer
      assert subject.stacksExtension == stacksExtension
      assert subject.project == project
      assert subject.repositoryHandler == repositoryHandler
  }

  @Test
  void 'should inject to all Aware interface in subject with custom fields'() {
    given:
      def injector = new StacksContainerInjector(project)
      def subject = new TestSubjectWithCustomField()

    when:
      injector.injectAll(subject)

    then:
      assert subject.extensionContainer == extensionContainer
  }

  static class TestSubject implements
      ExtensionContainerAware,
      PluginContainerAware,
      TaskContainerAware,
      StacksExtensionAware,
      ProjectAware,
      RepositoryHandlerAware {
    ExtensionContainer extensionContainer
    TaskContainer      taskContainer
    PluginContainer    pluginContainer
    StacksExtension    stacksExtension
    Project            project
    RepositoryHandler  repositoryHandler
  }

  static class TestSubjectWithCustomField implements
      ExtensionContainerAware {
    ExtensionContainer extensionContainer
    String             customField0
    int                customField1
  }

}

