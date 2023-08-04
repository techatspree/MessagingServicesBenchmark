package de.akquinet.msbenchmark.platform

interface MessagingService {
    abstract fun sendMessage(s: String)
    abstract fun waitForNumberOfReceivedMessages(nrOfSentMessages: Long)
}
