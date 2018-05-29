package ru.alfalab.gradle.platform.app

import com.github.tomakehurst.wiremock.http.Request
import com.github.tomakehurst.wiremock.http.RequestMethod
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.matching.MatchResult
import org.junit.Rule
import ru.alfalab.gradle.platform.tests.base.StacksGitIntegrationSpec
import spock.lang.Unroll

import static com.github.tomakehurst.wiremock.client.WireMock.*
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options

/**
 * @author tolkv
 * @version 20/12/2017
 */
class StacksApplicationPluginPublishingIntegSpec extends StacksGitIntegrationSpec {
  public static final String       DEFAULT_GROUP = 'ru.alfalab.test'
  @Rule               WireMockRule wireMockRule  = new WireMockRule(options().dynamicPort(), true)

  def setup() {
    createDocSubproject('lib0')
    createFile('lib0/src/docs/asciidoc/index.adoc') << '''# Intro'''
  }

  @Override
  void setupProject() {
    configureForVersion('0.1.0-SNAPSHOT')
    git.add(patterns: ['build.gradle', '.gitignore'] as Set)
  }

  @Unroll
  def 'should publish all artifacts from app type project for spring boot #springBootVersion'() {
    given:
      if (springBootVersion == '2XX') {
        createSpringBoot2XXWithCustomClassifier('app0')
      } else {
        createSpringBoot1XXWithCustomClassifier('app0')
      }

      wireMockRule.stubFor(get('/api/system/version')
                               .willReturn(aResponse()
                                               .withBodyFile('http/artifactory/api.system.version.json')
                                               .withStatus(200)
      ))

      // upload app jar
      wireMockRule.stubFor(requestMatching({ Request request ->
        if (request.method == RequestMethod.PUT &&
            request.url.startsWith('/snapshots/ru/alfalab/test/app0/0.1.0-SNAPSHOT/app0-0.1.0-SNAPSHOT-classifier.jar') &&
            request.url.contains('Module-Source=') &&
            request.url.contains('Module-Origin=') &&
            request.url.contains('build.number=') &&
            request.url.contains('version=0.1.0-SNAPSHOT') &&
            request.url.contains('platform.artifact.group=' + DEFAULT_GROUP) &&
            request.url.contains('platform.artifact.name=app0') &&
            request.url.contains('platform.label=api') &&
            request.url.contains('platform.deployment.id=ru.alfalab.test%3Ashould-publish-all-artifacts-from-app-type-project-for-spring-boot-' + springBootVersion + '/app0%3Aclassifier') &&
            request.url.contains('platform.service.id=ru.alfalab.test%3Ashould-publish-all-artifacts-from-app-type-project-for-spring-boot-' + springBootVersion + '/app0%3Aclassifier') &&
            request.url.contains('platform.deployment.app-name=should-publish-all-artifacts-from-app-type-project-for-spring-boot-' + springBootVersion) &&
            request.url.contains('platform.display-name=should-publish-all-artifacts-from-app-type-project-for-spring-boot-' + springBootVersion) &&
            request.url.contains('platform.artifact-type=service')) {
          return MatchResult.exactMatch()
        }
        return MatchResult.noMatch()
      }).willReturn(aResponse()
                        .withBodyFile('http/artifactory/put.snapshots.ru.alfalab.test.app0.json')
                        .withStatus(200)))

      // upload groovydoc jar
      wireMockRule.stubFor(requestMatching({ Request request ->
        if (request.method == RequestMethod.PUT &&
            request.url.startsWith('/snapshots/ru/alfalab/test/app0/0.1.0-SNAPSHOT/app0-0.1.0-SNAPSHOT-groovydoc.jar') &&
            request.url.contains('platform.artifact.group=' + DEFAULT_GROUP) &&
            request.url.contains('platform.artifact.name=app0') &&
            request.url.contains('platform.label=doc') &&
            request.url.contains('platform=true') &&
            request.url.contains('platform.display-name=should-publish-all-artifacts-from-app-type-project-for-spring-boot-' + springBootVersion + '') &&
            request.url.contains('platform.service.id=ru.alfalab.test%3Ashould-publish-all-artifacts-from-app-type-project-for-spring-boot-' + springBootVersion + '/app0%3Aclassifier') &&
            request.url.contains('platform.artifact-type=groovydoc')) {
          return MatchResult.exactMatch()
        }
        return MatchResult.noMatch()
      }).willReturn(aResponse()
                        .withBodyFile('http/artifactory/put.snapshots.ru.alfalab.test.app0.json')
                        .withStatus(200)))

      // upload javadoc jar
      wireMockRule.stubFor(requestMatching({ Request request ->
        if (request.method == RequestMethod.PUT &&
            request.url.startsWith('/snapshots/ru/alfalab/test/app0/0.1.0-SNAPSHOT/app0-0.1.0-SNAPSHOT-javadoc.jar') &&
            request.url.contains('build.number=') &&
            request.url.contains('platform.artifact.group=' + DEFAULT_GROUP) &&
            request.url.contains('platform.artifact.name=app0') &&
            request.url.contains('platform.label=doc') &&
            request.url.contains('platform=true') &&
            request.url.contains('platform.display-name=should-publish-all-artifacts-from-app-type-project-for-spring-boot-' + springBootVersion + '') &&
            request.url.contains('platform.service.id=ru.alfalab.test%3Ashould-publish-all-artifacts-from-app-type-project-for-spring-boot-' + springBootVersion + '/app0%3Aclassifier') &&
            request.url.contains('platform.artifact-type=javadoc')) {
          return MatchResult.exactMatch()
        }
        return MatchResult.noMatch()
      }).willReturn(aResponse()
                        .withBodyFile('http/artifactory/put.snapshots.ru.alfalab.test.app0.json')
                        .withStatus(200)))

      // upload source jar
      wireMockRule.stubFor(requestMatching({ Request request ->
        if (request.method == RequestMethod.PUT &&
            request.url.startsWith('/snapshots/ru/alfalab/test/app0/0.1.0-SNAPSHOT/app0-0.1.0-SNAPSHOT-sources.jar') &&
            request.url.contains('platform.artifact.group=' + DEFAULT_GROUP) &&
            request.url.contains('platform.artifact.name=app0') &&
            request.url.contains('platform.label=source') &&
            request.url.contains('platform=true') &&
            request.url.contains('platform.display-name=should-publish-all-artifacts-from-app-type-project-for-spring-boot-' + springBootVersion + '') &&
            request.url.contains('platform.service.id=ru.alfalab.test%3Ashould-publish-all-artifacts-from-app-type-project-for-spring-boot-' + springBootVersion + '/app0%3Aclassifier') &&
            request.url.contains('platform.artifact-type=sourcecode')) {
          return MatchResult.exactMatch()
        }
        return MatchResult.noMatch()
      }).willReturn(aResponse()
                        .withBodyFile('http/artifactory/put.snapshots.ru.alfalab.test.app0.json')
                        .withStatus(200)))

      // upload app0 pom
      wireMockRule.stubFor(requestMatching({ Request request ->
        if (request.method == RequestMethod.PUT &&
            request.url.startsWith('/snapshots/ru/alfalab/test/app0/0.1.0-SNAPSHOT/app0-0.1.0-SNAPSHOT.pom') &&
            request.url.contains('build.number=') &&
            request.url.contains('platform.service.id=ru.alfalab.test%3Ashould-publish-all-artifacts-from-app-type-project-for-spring-boot-' + springBootVersion + '/app0%3Aclassifier') &&
            !request.url.contains('platform.artifact.group=' + DEFAULT_GROUP) &&
            !request.url.contains('platform.artifact.name=app0') &&
            !request.url.contains('platform.label=source') &&
            !request.url.contains('platform=true') &&
            !request.url.contains('platform.display-name=should-publish-all-artifacts-from-app-type-project-for-spring-boot-' + springBootVersion + '') &&
            !request.url.contains('platform.artifact-type=sourcecode')) {
          return MatchResult.exactMatch()
        }
        return MatchResult.noMatch()
      }).willReturn(aResponse()
                        .withBodyFile('http/artifactory/put.snapshots.ru.alfalab.test.app0.json')
                        .withStatus(200)))

      wireMockRule.stubFor(requestMatching({ Request request ->
        if (request.method == RequestMethod.PUT &&
            request.url.startsWith('/snapshots/ru/alfalab/test/lib0/0.1.0-SNAPSHOT/lib0-0.1.0-SNAPSHOT-docs.zip') &&
            request.url.contains('build.number=') &&
            request.url.contains('platform.service.id=ru.alfalab.test%3Ashould-publish-all-artifacts-from-app-type-project-for-spring-boot-' + springBootVersion + '/app0%3Aclassifier')) {
          return MatchResult.exactMatch()
        }
        return MatchResult.noMatch()
      }).willReturn(aResponse()
                        .withBodyFile('http/artifactory/put.snapshots.ru.alfalab.test.app0.json')
                        .withStatus(200)))

      // publish build info
      wireMockRule.stubFor(put('/api/build')
                               .willReturn(aResponse()
                                               .withStatus(204)))

    expect:
      def successfully = runTasksSuccessfully('aP')

      successfully.wasExecuted('app0:build')
      successfully.wasExecuted('app0:bootRepackage') || successfully.wasExecuted('app0:bootJar') //sp1 and sp2 task
      wireMockRule.verify(1, putRequestedFor(urlMatching('/api/build')))
      wireMockRule.verify(1, putRequestedFor(urlMatching('/snapshots/ru/alfalab/test/app0/0.1.0-SNAPSHOT/app0-0.1.0-SNAPSHOT-classifier.jar.*')))
      wireMockRule.verify(1, putRequestedFor(urlMatching('/snapshots/ru/alfalab/test/app0/0.1.0-SNAPSHOT/app0-0.1.0-SNAPSHOT-classifier.jar.*platform.label=api.*')))
      wireMockRule.verify(1, putRequestedFor(urlMatching('/snapshots/ru/alfalab/test/app0/0.1.0-SNAPSHOT/app0-0.1.0-SNAPSHOT-classifier.jar.*platform.deployment.id=.*artifacts.*')))

      wireMockRule.verify(1, putRequestedFor(urlMatching('/snapshots/ru/alfalab/test/app0/0.1.0-SNAPSHOT/app0-0.1.0-SNAPSHOT-javadoc.jar.*')))
      wireMockRule.verify(1, putRequestedFor(urlMatching('/snapshots/ru/alfalab/test/app0/0.1.0-SNAPSHOT/app0-0.1.0-SNAPSHOT-javadoc.jar.*platform.label=doc.*')))
      wireMockRule.verify(1, putRequestedFor(urlMatching('/snapshots/ru/alfalab/test/app0/0.1.0-SNAPSHOT/app0-0.1.0-SNAPSHOT-javadoc.jar.*platform.artifact-type=javadoc.*')))

      wireMockRule.verify(1, putRequestedFor(urlMatching('/snapshots/ru/alfalab/test/app0/0.1.0-SNAPSHOT/app0-0.1.0-SNAPSHOT-sources.jar.*')))
      wireMockRule.verify(1, putRequestedFor(urlMatching('/snapshots/ru/alfalab/test/app0/0.1.0-SNAPSHOT/app0-0.1.0-SNAPSHOT-sources.jar.*platform.artifact-type=sourcecode.*')))
      wireMockRule.verify(1, putRequestedFor(urlMatching('/snapshots/ru/alfalab/test/app0/0.1.0-SNAPSHOT/app0-0.1.0-SNAPSHOT-sources.jar.*platform.label=source.*')))

      wireMockRule.verify(1, putRequestedFor(urlMatching('/snapshots/ru/alfalab/test/app0/0.1.0-SNAPSHOT/app0-0.1.0-SNAPSHOT-groovydoc.jar.*')))
      wireMockRule.verify(1, putRequestedFor(urlMatching('/snapshots/ru/alfalab/test/app0/0.1.0-SNAPSHOT/app0-0.1.0-SNAPSHOT-groovydoc.jar.*advanced-property.*')))
      wireMockRule.verify(1, putRequestedFor(urlMatching('/snapshots/ru/alfalab/test/app0/0.1.0-SNAPSHOT/app0-0.1.0-SNAPSHOT-groovydoc.jar.*platform.label=doc.*')))

      wireMockRule.verify(1, putRequestedFor(urlMatching('/snapshots/ru/alfalab/test/lib0/0.1.0-SNAPSHOT/lib0-0.1.0-SNAPSHOT-docs.zip.*')))
      wireMockRule.verify(1, putRequestedFor(urlMatching('/snapshots/ru/alfalab/test/lib0/0.1.0-SNAPSHOT/lib0-0.1.0-SNAPSHOT-docs.zip.*platform.advanced=advanced-docs-property.*')))

      wireMockRule.findUnmatchedRequests()?.requests?.forEach {
        println 'not found:'
        println '-' + it.url
      }

    where:
      springBootVersion || _
      '2XX'             || _
      '1XX'             || _
  }

  def configureForVersion(String version) {
    buildFile << """\

        allprojects { 
          group = '$DEFAULT_GROUP' 
          version = '$version'
        }
        
        project(':app0') {
          apply plugin: 'stacks.artifactory'
          artifactoryPublish {
                properties {
                  all '*:*:*:groovydoc@*', 'platform.advanced':'advanced-property'
                }
          }

          stacks {
            application {
              classifier = 'classifier'
            }
          }
                    
        }
        
        project(':lib0') {
          apply plugin: 'stacks.artifactory'
          apply plugin: 'stacks.doc.asciidoctor'
          artifactoryPublish {
              properties {
                all '*:*:*:*@*', 'platform.advanced':'advanced-docs-property'
              }
          }         
        }
        
        artifactory {
          contextUrl = 'http://localhost:${wireMockRule.port()}'
        }
        task snapshot { }
        task 'final'() { }
         
        """.stripIndent()

  }
}
