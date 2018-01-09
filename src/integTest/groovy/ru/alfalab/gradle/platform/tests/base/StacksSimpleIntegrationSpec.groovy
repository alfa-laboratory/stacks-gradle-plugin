package ru.alfalab.gradle.platform.tests.base

import nebula.test.IntegrationSpec
import ru.alfalab.gradle.platform.stack.application.StacksApplicationPlugin
import ru.alfalab.gradle.platform.stack.documentation.StacksDocumentationPlugin
import ru.alfalab.gradle.platform.stack.libraries.StacksLibrariesPlugin

/**
 * @author tolkv
 * @version 21/12/2017
 */
class StacksSimpleIntegrationSpec extends IntegrationSpec {
  protected void createAppSubprojectWithCustomClassifier(String name, String classifier) {
    addSubproject(
        name,
        """
      ${applyPlugin(StacksApplicationPlugin)}
      stacks {
        application {
          classifier = '$classifier'
        }
      }
      """.stripIndent()
    )
    writeHelloWorld("ru.stacks.test.apps.$name", file(name))
  }

  protected void createAppSubproject(String name) {
    addSubproject(
        name,
        """
      ${applyPlugin(StacksApplicationPlugin)}
      """.stripIndent()
    )
    writeHelloWorld("ru.stacks.test.apps.$name", file(name))
  }

  protected void createLibSubproject(String name) {
    addSubproject(
        name,
        """
      ${applyPlugin(StacksLibrariesPlugin)}
      """.stripIndent()
    )
    writeHelloWorld("ru.stacks.test.apps.$name", file(name))
  }


  protected void createDocSubproject(String name) {
    addSubproject(
        name,
        """
      ${applyPlugin(StacksDocumentationPlugin)}
      """.stripIndent()
    )
    writeHelloWorld("ru.stacks.test.apps.$name", file(name))
  }

  protected void createSubprojectWithPlugin(String name, Class plugin) {
    addSubproject(
        name,
        """
      ${applyPlugin(plugin)}
      """.stripIndent()
    )
    writeHelloWorld("ru.stacks.test.apps.$name", file(name))
  }

  protected void generateSevenProjectsTestGroup0() {
    createAppSubproject('app0')
    createAppSubproject('app1')
    createAppSubproject('app2')

    createLibSubproject('lib0')
    createLibSubproject('lib1')

    createDocSubproject('doc0')
    createDocSubproject('doc1')
  }

  boolean not(boolean v) {
    !v
  }

}
