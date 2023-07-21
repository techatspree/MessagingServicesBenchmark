# How to run


Start the Kafka server

    docker-compose up


# Technical Architecture

## Chosen Components

* Kafka Docker containers
  * We chose Bitnami because of its ranking in the Docker-Hub downloads.
* Apache Kafka Raft (KRaft)
  * We use KRaft instead of Zookeeper, because it is the more modern approach and by default enabled in the Docker container.
* Docker-Compose
  * A Kubernetes cluster on a local Minicube would be more realistic. But, because we wanted to measure the messaging services and had more knowledge with Docker-Compose we chose the later.   