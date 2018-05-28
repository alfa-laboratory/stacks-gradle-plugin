package ru.alfalab.gradle.platform.stack.application.spring

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import static org.mockito.Mockito.when

@RunWith(MockitoJUnitRunner)
class SpringBootVersionsHelperDslTest {
  @Mock        TaskContainer            tasks
  @Mock        Task                     repackageTask
  @Mock        Task                     bootJarTask
  @Mock        Project                  project
  @Mock        Action<Project>          springBoot1XXAction
  @Mock        Action<Project>          springBoot2XXAction
  @InjectMocks SpringBootVersionsHelper subject

  @Before
  void setUp() throws Exception {
    when(project.getTasks()).thenReturn(tasks);
  }

  @Test
  void 'should works'() {
    when(tasks.findByName("bootRepackage")).thenReturn(repackageTask);

    new SpringBootVersionsHelper(project).with {
      withSpringBoot1XXAction {}
      withSpringBoot2XXAction {}
      execute()
    }

  }
}
