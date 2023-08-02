import org.apache.kafka.clients.admin.Admin
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import org.apache.kafka.common.errors.TopicExistsException
import org.apache.kafka.common.serialization.StringSerializer
import java.util.*
import java.util.concurrent.ExecutionException

private const val NO_REPLICATION: Short = 1

private const val TOPIC_NAME = "MeasurementTopic"

fun main(args: Array<String>) {
    if (args.size > 0) {
        System.err.println("CLI arguments are not supported. I will ignore them and continue.")
    }

    println("Create topic...")
    val nrOfPartitions = 1
    val newTopic = NewTopic(TOPIC_NAME, nrOfPartitions, NO_REPLICATION)

    val kafkaConfiguration = createKafkaConfiguration()

    try {
        with(Admin.create(kafkaConfiguration)) {
            createTopics(listOf(newTopic)).all().get()
        }
        println("... topic created.")
    } catch (e: ExecutionException) {
        if (e.cause !is TopicExistsException) throw e
    }

    println("Send a message ....")
    KafkaProducer<String, String>(kafkaConfiguration).use { producer ->
        producer.send(
            ProducerRecord(
                TOPIC_NAME,
                "MeasurementKey",
                "my test message"
            )
        ) { recordMetadata: RecordMetadata, ex: Exception? ->
            when (ex) {
                null ->
                    println("Produced message with offset ${recordMetadata.offset()}")

                else ->
                    ex.printStackTrace()
            }
        }
    }
}

private fun createKafkaConfiguration(): Properties {
    val properties = Properties()
    properties[AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG] = "kafka:9092"
    properties[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.qualifiedName
    properties[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.qualifiedName
    return properties
}