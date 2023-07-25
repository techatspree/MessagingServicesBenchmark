pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    
}
rootProject.name = "MessagingServicesBenchmark"

include("Kafka:KafkaClient")
include("Kafka:KafkaMeasurement")
