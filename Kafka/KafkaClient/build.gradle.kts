plugins {
    kotlin("jvm") version "1.9.0"
    id("com.palantir.docker") version "0.35.0"
    application
}

group = "de.akquinet.playground"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.apache.kafka:kafka-clients:3.5.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
}

docker {
    name="akquinet/kafkaclient:latest"
    files(tasks.distTar.get())
    setDockerfile(project.file("src/main/docker/Dockerfile"))
}