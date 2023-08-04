package de.akquinet.msbenchmark.platform

import kotlin.test.Test
import kotlin.test.BeforeTest
import kotlin.test.assertNotNull
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest

@OptIn(ExperimentalCoroutinesApi::class)
internal class BenchmarkTest {
    private val testThreadContext = newSingleThreadContext("Test Thread")

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testThreadContext)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        testThreadContext.close()
    }

    @Test
    fun testBenchmark() {
        runBlocking {
            val testMessagingService = TestMessagingService(testThreadContext)
            val benchmark = Benchmark(testMessagingService)
            val results = benchmark.runBenchmark()
            assertNotNull(results)
        }
    }
}