# Infrastructure plugins for gradle

Plugins for implement next process:

* apply one of plugin – `stacks.app`, `stacks.doc`, `stacks.lib`
* consume meta information about project from artifactory. All properties (meta informatin in artifactory) are exporting after plugin above was applied
* use properties for search and construct your own pipeline process, including build,verify,audit,reporting,deploy,monitoring,etc stages

    _`simplify development process with auto configured project and follow best practices`_

See plugins description for more information

## Plugin `stacks.app`

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
    buildscript {
        repositories {
            
        }
        dependencies {
            
        }
    }
    apply plugin: 'stacks.app'

### Extension 



## Plugin `stacks.doc`

### Mission

### How to use

### 



## Plugin `stacks.lib`

### Mission

### How to use

### 


## Plugin `stacks.artifactory`

### Mission

### How to use

### 
