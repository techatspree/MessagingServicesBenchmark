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
        private val kafkaConfiguration : Properties = createKafkaConfiguration()
        private val consumer : KafkaConsumer<String,String>

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

    override fun sendMessage(messageValue: String) {
        KafkaProducer<String, String>(kafkaConfiguration).use { producer ->
            producer.send(
                ProducerRecord(
                    TOPIC_NAME,
                    MESSAGE_KEY,
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

    override fun waitForNumberOfReceivedMessages(nrOfSentMessages: Long) {
        val pollDuration = Duration.ofMillis(100)
        consumer.use {
            var totalCount = 0L
            while (totalCount<nrOfSentMessages) {
                // not the best method, but should work...
                totalCount = consumer
                    .poll(pollDuration)
                    .fold(totalCount, { nrOfReceivedMessages, message ->
                        println("Consumed record with key ${message.key()} and value ${message.value()}, while having $nrOfReceivedMessages received")
                        nrOfReceivedMessages + 1
                    })
                println("finished polling with totalCount=$totalCount")
            }
        }
    }
}