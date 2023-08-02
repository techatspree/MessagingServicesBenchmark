pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    
}
dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "MessagingServicesBenchmark"

include("Benchmark")
include("Kafka:KafkaClient")
include("Kafka:KafkaMeasurement")
