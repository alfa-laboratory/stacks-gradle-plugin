package ru.alfalab.gradle.platform.stack.base;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.alfalab.gradle.platform.stack.StacksContainerInjector;

/**
 * @author tolkv
 * @version 22/12/2017
 */
@Getter
@RequiredArgsConstructor
public class StacksConvention {
  public static final String STACKS_CONVENTION_NAME = "stacksConvention";
  private final StacksContainerInjector stacksInjector;
}
