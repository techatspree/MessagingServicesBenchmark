
# The Speed Benchmark

The main task is to send and receive 10**7 simple text messages consisting of the number 1 to 10**7. The order is not relevant. Each message must be received exactly once.

The main point here is speed. 

There is no need to be safe against failures of any system. Thus, messages do not need to be persisted. There is no need for transactions.

There is no need for security. Neither the messages nor the communication layers need to be decrypted.