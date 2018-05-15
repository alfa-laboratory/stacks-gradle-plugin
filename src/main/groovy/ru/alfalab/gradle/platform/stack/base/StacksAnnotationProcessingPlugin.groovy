package ru.alfalab.gradle.platform.stack.base

import org.gradle.api.tasks.compile.JavaCompile

class StacksAnnotationProcessingPlugin extends StacksAbstractPlugin {
  @Override
  void applyPlugin() {
    project.tasks.withType(JavaCompile) { JavaCompile t ->
      t.with {
        options.encoding = 'UTF-8'
        //For map struct and lombok
        if (!options.compilerArgs.contains('-Amapstruct.defaultComponentModel=spring')) {
          options.compilerArgs += '-Amapstruct.defaultComponentModel=spring'
        }
        if (!options.compilerArgs.contains('-Amapstruct.unmappedTargetPolicy=IGNORE')) {
          options.compilerArgs += '-Amapstruct.unmappedTargetPolicy=IGNORE'
        }
      }
    }
  }
}
