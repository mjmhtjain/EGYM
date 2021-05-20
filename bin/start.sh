#!/usr/bin/env bash

# Implement this function to specify the platform your service is built on.
select_platform() {
    # Uncomment one of the following commands to specify the language/version you depend on:
#    use_openjdk8
    use_openjdk11
#    use_go1_11_with_dep
    #use_go1_12_with_go_mod
}

# Implement this function so that when executed it would build and start your service.
run_service() {
    # The commands to build and start the service go here.
    cd ..
    mvn package assembly:single -f pom.xml
    cd target
    java -jar exercise-manager-1.0-SNAPSHOT.jar
}
