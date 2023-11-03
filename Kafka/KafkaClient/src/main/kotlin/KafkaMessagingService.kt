import de.akquinet.msbenchmark.platform.MessagingService
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


class KafkaMessagingService : MessagingService {
    companion object {
        private const val NO_REPLICATION: Short = 1
        private const val NR_OF_PARTITIONS = 1
        private const val TOPIC_NAME = "MeasurementBenchmarkTopic"
        private const val MESSAGE_KEY = "MeasurementKey"
        private val pollDuration = Duration.ofMillis(100)
        private val kafkaConfiguration: Properties = createKafkaConfiguration()
        private val consumer: KafkaConsumer<String, String>

        init {
            println("Create topic...")
            val newTopic = NewTopic(TOPIC_NAME, NR_OF_PARTITIONS, NO_REPLICATION)
            try {
                with(Admin.create(kafkaConfiguration)) {
                    createTopics(listOf(newTopic)).all().get()
                }
                println("... topic created.")
            } catch (e: ExecutionException) {
                if (e.cause !is TopicExistsException) throw e
            }

            println("Register consumer ...")
            consumer = KafkaConsumer<String, String>(kafkaConfiguration).apply {
                subscribe(listOf(TOPIC_NAME))
            }
            println("Registered consumer: $consumer")

            println("Register shutdown hook to free consumer")
            Runtime.getRuntime().addShutdownHook(Thread {
                // abort any consumer blocked in polling
                consumer.wakeup()
            })
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

    }

    override fun sendMessage(message: String) {
        KafkaProducer<String, String>(kafkaConfiguration).use { producer ->
            producer.send(
                ProducerRecord(
                    TOPIC_NAME,
                    MESSAGE_KEY,
                    message
                )
            ) { recordMetadata: RecordMetadata, ex: Exception? ->
                if (ex != null) {
                    // TODO: logging...
                    System.err.println("Error while producing message with offset ${recordMetadata.offset()}: $ex")
                    ex.printStackTrace()
                }
            }
        }
    }

    override fun waitForNumberOfReceivedMessages(nrOfSentMessages: Long) {
        var totalCount = 0L
        while (totalCount < nrOfSentMessages) {
            println("polling for messages")
            val records = consumer.poll(pollDuration)
            val recordsIterator = records.iterator()
            while (recordsIterator.hasNext() &&
                totalCount < nrOfSentMessages
            ) {
                recordsIterator.next()
                totalCount++
            }
            println("received now $totalCount of $nrOfSentMessages messages")
        }
        println("received all $nrOfSentMessages messages")
    }
}