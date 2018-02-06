# Infrastructure plugins for gradle

Plugins for implement next process:

* apply one of plugin – `stacks.app.spring-boot`, `stacks.doc`, `stacks.lib`
* consume meta information about project from artifactory. All properties (meta informatin in artifactory) are exporting after plugin above was applied
* use properties for search and construct your own pipeline process, including build,verify,audit,reporting,deploy,monitoring,etc stages

    _`simplify development process with auto configured project and follow best practices`_

See plugins description for more information

# General Plugins

## Plugin `stacks.app.spring-boot`

Apply this plugin to project or subproject which contained a deployable Spring Boot Application

### Mission'

Simplify Spring Boot Application configuration

* configure jar task – add classifier `app`
* add simple dependency management. See Unified DSL in extension section
* mark produced artifact, add next properties
    
    platform.artifact-type=API
    platform.deployment.app-name=project.name
    platform.repo.key=git project key (extract from remotes/origin)
    platform.repo.slug=git project slug (extract from remotes/origin)
    platform.label=APP
    version=project.version

Export additional info to `/info` endpoint in app. See `spring-boot-actuator`    

### How to use
    stacks {
      application {
        classifier = 'classifier'
        title = 'test-project'
      }
    }
    apply plugin: 'stacks.app.spring-boot'

### Extension 

    stacks {
      application {
        classifier = 'app'
        title = rootProject.name
      }
    }

| property   | use for                                              |
| ---        | ---                                                  |
| classifier | customize  artifact classifier                       |
| title      | customize `platform.display-name` artifact metadata  |

## Plugin `stacks.release`

Nebula Release backend plugin

### Mission

Configure release logic, apply `nebula.release` plugin and configure it for multiproject builds

### How to use
Requirements:

1. repository with initial commit
2. configured remote for check origin and push tags
3. configured authentication (public key/password/hardcoded/ssh-agent) for access to remote repo

        apply plugin: 'stacks.release`
    
        >(0) $ ./gradlew snapshot
        >(1) $ ./gradlew candidate
        >(2) $ ./gradlew final
    
1. build -SNAPSHOT artifact (use repo from `stacks.repositories.snapshot` section provided by `stacks.artifactory` plugin)
2. build -rc artifact (use repo from `stacks.repositories.releases` section provided by `stacks.artifactory` plugin)
3. build release artifact (use repo from `stacks.repositories.releases` section provided by `stacks.artifactory` plugin)


## Plugin `stacks.artifactory`

Configure artifactory tasks and publish logic

### Mission

Configure artifactory tasks, publish repositories and integrate with other plugins
Use it with `stacks.publications` plugin and `stacks.release`

### How to use

    apply plugin: 'stacks.artifactory`

    > $ ./gradlew artifactoryPublish

or apply `stacks.release` plugin and use
    
    > $ ./gradlew snapshot
    > $ ./gradlew candidate
    > $ ./gradlew final
    
### Extension

Customise default publish repositories
     
     stacks {
        repositories {
          snapshot 'libs-snapshot-local'
          release 'libs-release-local'
        }
     }
    
use libs layout `libs-release-local` and `libs-snapshot-local`
        
     stacks {
        repositories {
          useLibsRepositories()
        }
     }

use plugins layout `plugins-release-local` and `plugins-snapshot-local`
      
     stacks {
        repositories {
          usePluginsRepositories()
        }    
     }
     
Default values can by override by artifactory section in user `build.gradle` file too


## Plugin `stacks.dependencies`

Configure Dependency Management Plugin
May be useful for batch dependency configuration for

* spring

### Mission

Configure pack of dependencies for some frameworks. Provide default dependencies.
Add generic dependencies like lombok

### How to use

    apply plugin: 'stacks.dependencies'
    
Resolve spring boot dependencies version according next order

1. from `stacks.spring.bootVersion`
2. from `ext.springBootVersion` if `stacks.spring.bootVersion` not set     

### Extension 

     stacks {
        spring {
          cloudVersion 'Edgware.SR1'  //<-- default value
          bootVersion '1.5.9.RELEASE' //<-- default value
        }    
     }

## Plugin `stacks.lib`

### Mission

### How to use

###
## Plugin `stacks.doc`

### Mission

### How to use


# Side Plugins

## Plugin `stacks.project.version-to-file`

Create `project-version` file in rootProject build directory

### Mission

Help to resolve project version after build

### How to use

    apply plugin: 'stacks.project.version-to-file'

## Plugin `stacks.publications`

Configure default publications for project. Recommend to use with `stacks.artifactory`

### Mission

Easy publications configuration

1. auto create publications for java, groovy. Support jar/war
2. provide name and description from project to published pom.xml
3. export project dependencies to pom.xml from compile/compileOnly/provided/compileApi configurations. Export as provided/runtime scopes in maven 
4. export developer info – name, email, role (use `nebula.contacts` plugin) to pom.xml
5. Add meta about git repo to resulted jar
 
### How to use

    apply plugin: 'stacks.project.version-to-file'


## Plugin `stacks.info`

Simple plugin based on nebula info plugins for add advanced meta info for artifacts

### Mission

Add meta information about environment, git, dependencies, scm, ci, java and export it to Jar file as properties and manifest file. 
Also extract all information to artifacts metadata in artifactory (need stacks.artifactory plugin)

### How to use

    apply plugin: 'stacks.info'
    

## Plugin `stacks.lang.groovy`

Configure groovy and java compile tasks, environment, annotation processors configuration (mapstruct and lombok), tasks for export javadoc and groovy doc. Configure publish logic
Additional – configure codenarc for read config/codenarc/codenarc.xml file and apply if it exists

### Mission

Simplify project configuration for groovy or java development process. Provide artifacts with docs for developed artifacts by default
Reduce boilerplate and configure APT config
Add dependencyManagement plugin for manage dependencies
Apply `stacks.dependencies` plugin

### How to use

    apply plugin: 'stacks.lang.groovy'
   
 