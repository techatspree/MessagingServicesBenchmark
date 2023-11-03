package de.akquinet.msbenchmark.platform

interface MessagingService {
    fun sendMessage(message: String)
    fun waitForNumberOfReceivedMessages(nrOfSentMessages: Long)
}
