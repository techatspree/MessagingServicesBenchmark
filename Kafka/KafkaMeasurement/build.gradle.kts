plugins {
    id("com.palantir.docker-compose") version "0.35.0"
}

group = "de.akquinet.playground"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    docker(project(":Kafka:KafkaClient"))
}

dockerCompose {
    // not really used
    setTemplate(project.file("src/main/docker/docker-compose.yml.template"))
    setDockerComposeFile(project.file("build/docker-compose.yml"))
}