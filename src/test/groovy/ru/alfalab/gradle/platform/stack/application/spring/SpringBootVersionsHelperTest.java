package ru.alfalab.gradle.platform.stack.application.spring;

import org.gradle.api.Action;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskContainer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SpringBootVersionsHelperTest {
  @Mock        TaskContainer            tasks;
  @Mock        Task                     repackageTask;
  @Mock        Task                     bootJarTask;
  @Mock        Project                  project;
  @Mock        Action<Project>          springBoot1XXAction;
  @Mock        Action<Project>          springBoot2XXAction;
  @InjectMocks SpringBootVersionsHelper subject;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() throws Exception {
    when(project.getTasks()).thenReturn(tasks);

    subject
        .withSpringBoot1XXAction(springBoot1XXAction)
        .withSpringBoot2XXAction(springBoot2XXAction);
  }

  @Test
  public void should_resolve_sp1_by_repackage_task() {
    //given
    when(tasks.findByName("bootRepackage")).thenReturn(repackageTask);

    //when
    subject.execute();

    //then
    verify(springBoot1XXAction, times(1)).execute(anyObject());
  }

  @Test
  public void should_resolve_sp2_by_bootJar_task() {
    //given
    when(tasks.findByName("bootJar")).thenReturn(bootJarTask);

    //when
    subject.execute();

    //then
    verify(springBoot2XXAction, times(1)).execute(anyObject());
  }


  @Test
  public void should_fail_when_thereis_a_bootJar_and_bootRepackage_tasks() {
    //given
    when(tasks.findByName("bootJar")).thenReturn(bootJarTask);
    when(tasks.findByName("bootRepackage")).thenReturn(repackageTask);

    //expect
    thrown.expect(GradleException.class);
    thrown.expectMessage(
        containsString("Can't determine Spring Boot version — 1XX or 2XX. I might be because bootJar[2XX] and bootRepackage[1xx] exists in one project")
    );

    //when
    subject.execute();
  }

  @Test
  public void should_works_without_handlers_sp1() {
    when(tasks.findByName("bootRepackage")).thenReturn(bootJarTask);
    new SpringBootVersionsHelper(project).execute();
  }


  @Test
  public void should_works_without_handlers_sp2() {
    when(tasks.findByName("bootJar")).thenReturn(bootJarTask);
    new SpringBootVersionsHelper(project).execute();
  }

  @Test
  public void should_works_without_sp2_handler() {
    when(tasks.findByName("bootJar")).thenReturn(bootJarTask);
    new SpringBootVersionsHelper(project)
        .withSpringBoot1XXAction(springBoot1XXAction)
        .execute();
  }

  @Test
  public void should_works_without_sp1_handler() {
    when(tasks.findByName("bootRepackage")).thenReturn(bootJarTask);
    new SpringBootVersionsHelper(project)
        .withSpringBoot2XXAction(springBoot2XXAction)
        .execute();
  }
}