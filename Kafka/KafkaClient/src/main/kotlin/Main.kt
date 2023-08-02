import org.apache.kafka.clients.admin.Admin
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import org.apache.kafka.common.errors.TopicExistsException
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import java.time.Duration
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

    println("Register consumer ...")
    val consumer = KafkaConsumer<String, String>(kafkaConfiguration).apply {
        subscribe(listOf(TOPIC_NAME))
    }
    println("Consumer: $consumer")

    println("Send two messages ....")
    sendMessage(kafkaConfiguration, "my test message 1")
    sendMessage(kafkaConfiguration, "my test message 2")

    println("Start receiving messages with an infinite loop")
    val pollDuration = Duration.ofMillis(100)
    consumer.use {
        var totalCount = 0L
        while (true) {
            totalCount = consumer
                .poll(pollDuration)
                .fold(totalCount, { nrOfReceivedMessages, message ->
                    println("Consumed record with key ${message.key()} and value ${message.value()}, while having $nrOfReceivedMessages received")
                    nrOfReceivedMessages + 1
                })
        }
    }
    println("After infinity .... you should not read this...")
}

private fun sendMessage(kafkaConfiguration: Properties, messageValue: String) {
    KafkaProducer<String, String>(kafkaConfiguration).use { producer ->
        producer.send(
            ProducerRecord(
                TOPIC_NAME,
                "MeasurementKey",
                messageValue
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
    // basic properties
    properties[AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG] = "kafka:9092"
    // for the sender
    properties[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.qualifiedName
    properties[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.qualifiedName
    // for the consumer
    properties[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
    properties[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.qualifiedName
    properties[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.qualifiedName
    properties[ConsumerConfig.GROUP_ID_CONFIG] = "akquinet_benchmark_group_1"
    return properties
}