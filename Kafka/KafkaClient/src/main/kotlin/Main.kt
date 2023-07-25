import org.apache.kafka.clients.admin.Admin
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.common.errors.TopicExistsException
import java.util.*
import java.util.concurrent.ExecutionException

private const val NO_REPLICATION:Short = 1

fun main(args : Array<String>) {
    if (args.size > 0 ) {
        System.err.println("CLI arguments are not supported. I will ignore them and continue.")
    }

    println("Create topic...")
    val nrOfPartitions = 1
    val newTopic = NewTopic("MeasurementTopic", nrOfPartitions, NO_REPLICATION)
    val kafkaConfiguration = Properties()
    kafkaConfiguration.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092")
    try {
        with(Admin.create(kafkaConfiguration)) {
            createTopics(listOf(newTopic)).all().get()
        }
    } catch (e: ExecutionException) {
        if (e.cause !is TopicExistsException) throw e
    }
}