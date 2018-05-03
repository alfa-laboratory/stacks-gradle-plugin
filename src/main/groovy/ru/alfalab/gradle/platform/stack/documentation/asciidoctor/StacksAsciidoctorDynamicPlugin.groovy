package ru.alfalab.gradle.platform.stack.documentation.asciidoctor

import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.PluginContainer
import ru.alfalab.gradle.platform.stack.api.PluginContainerAware
import ru.alfalab.gradle.platform.stack.base.StacksAbstractPlugin

/**
 * @author tolkv
 * @version 27/04/2018
 */
class StacksAsciidoctorDynamicPlugin extends StacksAbstractPlugin implements PluginContainerAware {
  PluginContainer pluginContainer

  @Override
  void applyPlugin() {

    pluginContainer.withId('org.asciidoctor.convert') {
      pluginContainer.apply 'com.github.jruby-gradle.base'

      project.jruby {
        defaultRepositories false
      }

      project.asciidoctor {
        inputs.dir file('build/generated-snippets')
        outputDir 'build/asciidoc'
        sourceDir 'src/docs/asciidoc'
        requires 'asciidoctor-diagram'

        gemPath = project.tasks.jrubyPrepare.outputDir

        attributes 'source-highlighter': 'coderay',
                   'imagesdir': 'images',
                   'toc': 'left',
                   'icons': 'font',
                   'setanchors': 'true',
                   'idprefix': '',
                   'idseparator': '-',
                   'docinfo1': 'true',
                   'toclevels': 5,
                   'snippets': file('build/generated-snippets')

        dependsOn project.tasks.jrubyPrepare
        mustRunAfter project.tasks.jrubyPrepare //only for specific cases, dependsOn is more strong requirement
      }

    }

    pluginContainer.withType(JavaPlugin) {
      project.asciidoctor.dependsOn project.test
    }

  }
}
