# Infrastructure plugins for gradle

Plugins for implement next process:

* apply one of plugin – `stacks.app`, `stacks.doc`, `stacks.lib`
* consume meta information about project from artifactory. All properties (meta informatin in artifactory) are exporting after plugin above was applied
* use properties for search and construct your own pipeline process, including build,verify,audit,reporting,deploy,monitoring,etc stages

    _`simplify development process with auto configured project and follow best practices`_

See plugins description for more information

# General Plugins

## Plugin `stacks.app`

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
    

### How to use
    stacks {
      application {
        classifier = 'classifier'
        title = 'test-project'
      }
    }
    apply plugin: 'stacks.app'

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

### Mission

### How to use

    apply plugin: 'stacks.release`

## Plugin `stacks.artifactory`

### Mission

### How to use

    apply plugin: 'stacks.artifactory`

### Extension

Customise default publish repositories
     
     stacks {
        repositories {
          snapshot 'libs-snapshot-local'
          releases 'libs-release-local'
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
     
Defauly values can by override by artifactory section in user `build.gradle` file too


## Plugin `stacks.lib`

### Mission

### How to use

### 


###
## Plugin `stacks.doc`

### Mission

### How to use

### 

# Side Plugins

###
## Plugin `stacks.info`

Simple plugin based on nebula info plugins for add advanced meta info for artifacts

### Mission

Add meta information about environment, git, dependencies, scm, ci, java and export it to Jar file as properties and manifest file. 
Also extract all information to artifacts metadata in artifactory (need stacks.artifactory plugin)

### How to use

    apply plugin: 'stacks.info'
    
### Extension

