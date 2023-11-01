package de.akquinet.msbenchmark.platform

import kotlin.system.measureTimeMillis

class Benchmark(val messagingService: MessagingService) {
    fun runBenchmark(): BenchmarkResult {
        val nrOfMeasurements = 10L
        val nrOfSentMessages = 1000L

        val executionTimes = (1..nrOfMeasurements)
            .fold(emptyList<Long>()) { executionTimes, _ ->
                executionTimes.plus(measureTimeMillis {
                    doMeasurement(nrOfSentMessages)
                })
            }

        return BenchmarkResult(nrOfMeasurements, nrOfSentMessages, executionTimes.computeExcludedQuartiles())
    }

    private fun doMeasurement(nrOfSentMessages: Long) {
        (1..nrOfSentMessages)
            .forEach { messageNr -> messagingService.sendMessage("Message $messageNr") }
        messagingService.waitForNumberOfReceivedMessages(nrOfSentMessages)
    }
}