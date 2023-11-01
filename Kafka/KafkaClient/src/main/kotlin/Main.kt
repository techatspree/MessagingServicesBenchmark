import de.akquinet.msbenchmark.platform.Benchmark

private const val NO_REPLICATION: Short = 1

private const val TOPIC_NAME = "MeasurementTopic"

fun main(args: Array<String>) {
    if (args.size > 0) {
        System.err.println("CLI arguments are not supported. I will ignore them and continue.")
    }

    val kafkaMessagingService = KafkaMessagingService()
    val benchmark = Benchmark(kafkaMessagingService)
    val results = benchmark.runBenchmark()
    println("results = ${results}")
}

