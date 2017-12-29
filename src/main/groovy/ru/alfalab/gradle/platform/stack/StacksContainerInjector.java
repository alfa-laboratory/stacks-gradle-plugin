package ru.alfalab.gradle.platform.stack;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.logging.Logger;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.tasks.TaskContainer;
import ru.alfalab.gradle.platform.stack.api.*;
import ru.alfalab.gradle.platform.stack.base.StacksExtension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

/**
 * Add injection support for new type:
 * 1. Add Aware interface to {@link #supportedInterfaces}
 * 2. Add logging in {@link #StacksContainerInjector(Project)}
 * 3. Put actual value for inject into {@link #values}
 * 4. Test it {@link StacksInjectorUnitTest}
 *
 * @author tolkv
 * @version 22/12/2017
 */
@Slf4j
@ToString
public class StacksContainerInjector {
  @Getter private final ExtensionContainer extensionContainer;
  @Getter private final PluginContainer    pluginContainer;
  @Getter private final TaskContainer      taskContainer;
  @Getter private final StacksExtension    stacksExtension;
  @Getter private final RepositoryHandler  repositoryHandler;

  private final Map<Class<?>, Method> accessors;
  private final Map<Class<?>, Object> values;

  private List<Class<?>> supportedInterfaces = asList(
      ExtensionContainerAware.class,
      PluginContainerAware.class,
      TaskContainerAware.class,
      ProjectAware.class,
      StacksExtensionAware.class,
      RepositoryHandlerAware.class
  );

  public StacksContainerInjector(Project project) {
    Logger logger = project.getLogger();

    extensionContainer = project.getExtensions();
    pluginContainer = project.getPlugins();
    taskContainer = project.getTasks();
    stacksExtension = project.getExtensions().findByType(StacksExtension.class);
    repositoryHandler = project.getRepositories();

    logger.debug("\tconfigure extension container for inject: {}", extensionContainer);
    logger.debug("\tconfigure plugin      container   for inject: {}", pluginContainer);
    logger.debug("\tconfigure task        container   for inject: {}", taskContainer);
    logger.debug("\tconfigure extension   container   for inject: {}", extensionContainer);
    logger.debug("\tconfigure extension   stacks      for inject: {}", stacksExtension);
    logger.debug("\tconfigure repository  handler     for inject: {}", repositoryHandler);

    accessors = supportedInterfaces.stream()
        .flatMap(aClass -> stream(aClass.getDeclaredMethods()))
        .filter(method -> method.getName().startsWith("set") && method.getParameterCount() == 1)
        .collect(toMap(
            o -> o.getParameterTypes()[0],
            o -> o
        ));

    values = new HashMap<>();
    values.put(ExtensionContainer.class, extensionContainer);
    values.put(PluginContainer.class, pluginContainer);
    values.put(TaskContainer.class, taskContainer);
    values.put(StacksExtension.class, stacksExtension);
    values.put(Project.class, project);
    values.put(RepositoryHandler.class, repositoryHandler);
  }

  public void injectAll(final Object target) {
    Arrays.stream(target.getClass().getDeclaredMethods())
        .filter(method -> method.getParameterCount() == 1 && method.getName().startsWith("set"))
        .filter(method -> accessors.containsKey(method.getParameterTypes()[0]))
        .forEach(method -> {
          Class<?> argumentType = method.getParameterTypes()[0];
          Method   setter       = accessors.get(argumentType);
          if (setter != null) {
            try {
              setter.invoke(target, values.get(argumentType));
            } catch (IllegalAccessException | InvocationTargetException e) {
              log.error("error during injection to {} with {}", target, setter.getName());
              log.error("injection was failed because", target);
            }
          }
        });
  }

}
