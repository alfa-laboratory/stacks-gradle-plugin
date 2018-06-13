package ru.alfalab.gradle.platform.stack.libraries

import nebula.test.PluginProjectSpec
import org.gradle.api.publish.maven.MavenArtifact
import org.gradle.api.publish.maven.MavenPublication

class StacksLibrariesPluginTest extends PluginProjectSpec {

    def 'should configure publishing for jar artifact'() {

        given:
        project.version = '0.1.1-SNAPSHOT'

        when:
        project.apply plugin: 'stacks.lib'

        then: 'nebula publication should exist'
        def nebulaPublication = project.publishing.publications.find { it.name == 'nebula' } as MavenPublication
        nebulaPublication != null

        and: 'should configure valid target for jar publication'
        def targetFileName = 'should-configure-publishing-for-jar-artifact-0.1.1-SNAPSHOT.jar'
        def all = nebulaPublication.artifacts.findAll { it.file.name.contains targetFileName } as List<MavenArtifact>

        all.size() == 1
        all.first().extension == 'jar'
    }

    @Override
    String getPluginName() {
        return 'stacks.lib'
    }
}
