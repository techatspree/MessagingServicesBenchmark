package de.akquinet.msbenchmark.platform

import kotlin.test.Test
import kotlin.test.assertNotNull

internal class BenchmarkTest {
    @Test
    public fun testBenchmark() {
        val testMessagingService = TestMessagingService()
        val benchmark = Benchmark(testMessagingService)
        val results = benchmark.runBenchmark()
        assertNotNull(results)
    }
}