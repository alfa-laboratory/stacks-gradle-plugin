package ru.alfalab.gradle.platform.app

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.http.Request
import com.github.tomakehurst.wiremock.http.RequestMethod
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.matching.MatchResult
import com.github.tomakehurst.wiremock.matching.RequestPattern
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder
import com.github.tomakehurst.wiremock.matching.UrlPathPattern
import com.github.tomakehurst.wiremock.matching.ValueMatcher
import org.junit.Rule
import ru.alfalab.gradle.platform.tests.base.StacksGitIntegrationSpec
import ru.alfalab.gradle.platform.tests.base.StacksSimpleIntegrationSpec
import static com.github.tomakehurst.wiremock.client.WireMock.*
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

/**
 * @author tolkv
 * @version 20/12/2017
 */
class StacksApplicationPluginPublishingIntegSpec extends StacksGitIntegrationSpec {
  public static final String       DEFAULT_GROUP = 'ru.alfalab.test'
  @Rule               WireMockRule wireMockRule  = new WireMockRule(options().dynamicPort(), true)

  def setup() {
    createAppSubproject('app0')
    createLibSubproject('lib0')
  }

  @Override
  void setupProject() {
    configureForVersion('0.1.0-SNAPSHOT')
    git.add(patterns: ['build.gradle', '.gitignore'] as Set)
  }

  def 'should publish all artifacts from app type project'() {
    given:
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
            request.url.contains('platform.deployment.id=ru.alfalab.test%3Ashould-publish-all-artifacts-from-app-type-project/app0%3Aclassifier') &&
            request.url.contains('platform.deployment.app-name=should-publish-all-artifacts-from-app-type-project') &&
            request.url.contains('platform.display-name=should-publish-all-artifacts-from-app-type-project') &&
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
            request.url.contains('platform.display-name=should-publish-all-artifacts-from-app-type-project') &&
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
            request.url.contains('platform.display-name=should-publish-all-artifacts-from-app-type-project') &&
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
            request.url.contains('platform.display-name=should-publish-all-artifacts-from-app-type-project') &&
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
            !request.url.contains('platform.artifact.group=' + DEFAULT_GROUP) &&
            !request.url.contains('platform.artifact.name=app0') &&
            !request.url.contains('platform.label=source') &&
            !request.url.contains('platform=true') &&
            !request.url.contains('platform.display-name=should-publish-all-artifacts-from-app-type-project') &&
            !request.url.contains('platform.artifact-type=sourcecode')) {
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

    when:
      def successfully = runTasksSuccessfully('aP')

    then:
      successfully.wasExecuted('app0:build')
      successfully.wasExecuted('app0:bootRepackage')
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
      wireMockRule.verify(1, putRequestedFor(urlMatching('/snapshots/ru/alfalab/test/app0/0.1.0-SNAPSHOT/app0-0.1.0-SNAPSHOT-groovydoc.jar.*advanced.*')))

      wireMockRule.findUnmatchedRequests()?.requests?.forEach {
        println 'not found:'
        println '-' + it.url
      }

//      wireMockRule.verify(1, putRequestedFor(urlMatching('/snapshots/ru/alfalab/test/app0/0.1.0-SNAPSHOT/app0-0.1.0-SNAPSHOT-groovydoc.jar.*')))

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
        
        artifactory {
          contextUrl = 'http://localhost:${wireMockRule.port()}'
        }
        task snapshot { }
        task 'final'() { }
         
        """.stripIndent()

  }
}
