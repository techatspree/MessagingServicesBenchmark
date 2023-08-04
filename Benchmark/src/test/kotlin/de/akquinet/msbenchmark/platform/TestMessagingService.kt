package de.akquinet.msbenchmark.platform

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel


class TestMessagingService(val testThreadContext: CloseableCoroutineDispatcher) : MessagingService {

    private val channel = Channel<String>()
    private var receivedMessages = 0L
    private val coroutineScope = CoroutineScope(testThreadContext)

    override fun sendMessage(s: String) {
        coroutineScope.launch {
            channel.send(s)
        }
    }

    override fun waitForNumberOfReceivedMessages(nrOfSentMessages: Long) {
        coroutineScope.launch {
            while ( receivedMessages <= nrOfSentMessages ) {
                channel.receive()
                receivedMessages++
            }
        }
    }
}
