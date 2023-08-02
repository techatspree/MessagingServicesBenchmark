plugins {
    alias(libs.plugins.palantir.docker)
    id("com.palantir.docker-compose") // do nbot specify the version, there seems to be a classpath problem with the docker main plugin
}

group = "de.akquinet.playground"
version = "1.0-SNAPSHOT"

dependencies {
   docker(project(":Kafka:KafkaClient"))
}

dockerCompose {
    // not really used
    setTemplate(project.file("src/main/docker/docker-compose.yml.template"))
    setDockerComposeFile(project.file("build/docker-compose.yml"))
}