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
      apply plugin: 'org.springframework.boot'
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

  protected void createSpringBoot1XXWithCustomClassifier(String name, String classifier = null) {
    addSubproject(
        name,
        """
      buildscript {
        repositories {
          maven {
            url "https://plugins.gradle.org/m2/"
          }
        }
        dependencies {
          classpath "org.springframework.boot:spring-boot-gradle-plugin:1.5.10.RELEASE"
        }
      }
      apply plugin: 'org.springframework.boot'
      ${applyPlugin(StacksApplicationPlugin)}

      ${->
          if (classifier) {
            return """stacks {
            application {
              classifier = '$classifier'
            }
          }""".stripIndent()
          }
          return ''
        }
      dependencies {
        compile 'org.springframework.boot:spring-boot-starter-web'
      }
      """.stripIndent()
    )

    writeSpringBootApplicationFile(name)
  }

  protected void createSpringBoot2XXWithCustomClassifier(String name, String classifier = null) {
    addSubproject(
        name,
        """
      buildscript {
        repositories {
          maven {
            url "https://plugins.gradle.org/m2/"
          }
        }
        dependencies {
          classpath "org.springframework.boot:spring-boot-gradle-plugin:2.0.2.RELEASE"
        }
      }

      apply plugin: 'org.springframework.boot'
      ${applyPlugin(StacksApplicationPlugin)}
     ${->
          if (classifier) {
            return """stacks {
            application {
              classifier = '$classifier'
            }
          }""".stripIndent()
          }
          return ''
        }
      dependencies {
        compile 'org.springframework.boot:spring-boot-starter-web'
      }
      """.stripIndent()
    )

    writeSpringBootApplicationFile(name)
  }

  protected void createAppSubproject(String name) {
    addSubproject(
        name,
        """
      buildscript {
        repositories {
          maven {
            url "https://plugins.gradle.org/m2/"
          }
        }
        dependencies {
          classpath "org.springframework.boot:spring-boot-gradle-plugin:1.5.10.RELEASE"
        }
      }
      apply plugin: 'java'
      apply plugin: 'org.springframework.boot'
      ${applyPlugin(StacksApplicationPlugin)}
      dependencies {
        compile 'org.springframework.boot:spring-boot-starter-web'
      }
      """.stripIndent()

    )
    writeSpringBootApplicationFile(name)
  }

  private void writeSpringBootApplicationFile(String name) {
    def packageDotted = "ru.stacks.test.apps.$name"
    def path = './' + name + '/src/main/java/' + packageDotted.replace('.', '/') + '/HelloWorld.java'
    def javaFile = createFile(path, getProjectDir())
    javaFile << """\
            package ${packageDotted};
            import org.springframework.boot.autoconfigure.SpringBootApplication;
            
            @SpringBootApplication
            public class HelloWorld {
                public static void main(String[] args) {
                    System.out.println("Hello Integration Test");
                }
            }
            """.stripIndent()
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
