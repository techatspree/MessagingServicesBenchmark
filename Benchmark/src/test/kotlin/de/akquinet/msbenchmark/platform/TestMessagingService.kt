package de.akquinet.msbenchmark.platform


class TestMessagingService : MessagingService {
    private val messageQueue = ArrayDeque<String>()
    private var receivedMessages = 0L
    override fun sendMessage(s: String) {
        messageQueue.addLast(s)
    }

    override fun waitForNumberOfReceivedMessages(nrOfSentMessages: Long) {
        while ( receivedMessages <= nrOfSentMessages ) {

        }
        TODO("Not yet implemented")
    }

}
