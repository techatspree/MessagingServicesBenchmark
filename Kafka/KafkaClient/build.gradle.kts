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
    name="akquinet:kafkaclient"
    files(tasks.distTar.get())
    setDockerfile(project.file("src/main/docker/Dockerfile"))
}