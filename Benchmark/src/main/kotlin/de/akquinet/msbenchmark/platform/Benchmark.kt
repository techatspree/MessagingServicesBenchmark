package de.akquinet.msbenchmark.platform

import kotlin.system.measureTimeMillis

class Benchmark(val messagingService: MessagingService) {
    fun runBenchmark(): BenchmarkResult {
        val nrOfMeasurements = 10
        val nrOfSentMessages = 10L

        val executionTimes = (1..nrOfMeasurements)
            .fold(emptyList<Long>()) { executionTimes, number ->
                executionTimes.plus(measureTimeMillis {
                    doMeasurement(nrOfSentMessages)
                })
            }

        return BenchmarkResult(executionTimes.average())
    }

    private fun doMeasurement(nrOfSentMessages: Long) {
        (1..nrOfSentMessages)
            .forEach { messageNr -> messagingService.sendMessage("Message $messageNr") }
        messagingService.waitForNumberOfReceivedMessages(nrOfSentMessages)
    }
}