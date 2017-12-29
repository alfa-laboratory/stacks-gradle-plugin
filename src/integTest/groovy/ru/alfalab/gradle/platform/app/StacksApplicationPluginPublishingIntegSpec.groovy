package ru.alfalab.gradle.platform.app

import com.github.tomakehurst.wiremock.client.MappingBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.http.Request
import com.github.tomakehurst.wiremock.http.RequestMethod
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.matching.MatchResult
import com.github.tomakehurst.wiremock.matching.UrlPathPattern
import com.github.tomakehurst.wiremock.matching.ValueMatcher
import org.junit.Rule
import ru.alfalab.gradle.platform.tests.base.StacksSimpleIntegrationSpec
import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * @author tolkv
 * @version 20/12/2017
 */
class StacksApplicationPluginPublishingIntegSpec extends StacksSimpleIntegrationSpec {
  public static final String       DEFAULT_GROUP = 'ru.alfalab.test'
  @Rule               WireMockRule wireMockRule  = new WireMockRule()

  def setup() {
    createAppSubproject('app0')
    createLibSubproject('lib0')
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
            request.url.startsWith('/snapshots/ru/alfalab/test/app0/0.1.0-SNAPSHOT/app0-0.1.0-SNAPSHOT-app.jar') &&
            request.url.contains('build.number=') &&
            request.url.contains('platform.artifact.group=' + DEFAULT_GROUP) &&
            request.url.contains('platform.artifact.name=app0') &&
            request.url.contains('platform.label=api') &&
            request.url.contains('platform.deployment.id=ru.alfalab.test%3Aapp0%3Aapp') &&
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

      configureForVersion('0.1.0-SNAPSHOT')

    when:
      def successfully = runTasksSuccessfully('aP')

    then:
      successfully.wasExecuted('app0:build')
      successfully.wasExecuted('app0:bootRepackage')
  }

  def configureForVersion(String version) {
    buildFile << """\

        allprojects { 
          group = '$DEFAULT_GROUP' 
          version = '$version'
        }
        
        project(':app0') {
          apply plugin: 'stacks.artifactory'
        }
        
        artifactory {
          contextUrl = 'http://localhost:${wireMockRule.port()}'
        }
        task snapshot { }
        task 'final'() { }
         
        """.stripIndent()

  }
}
