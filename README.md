# kafka01

## Commands

Start Zookeeper Server - Where the messages are saved:

```shell
$ bin/zookeeper-server-start.sh config/zookeeper.properties
```

Start Kafka Server:
```shell
$ bin/kafka-server-start.sh config/server.properties
```

Create new topic:
```shell
$ bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic robohorta_new_event
```

Start producer console:
```shell
$ bin/kafka-console-producer.sh --broker-list localhost:9092 --topic robohorta_new_event

$ > message1,title1,etc
$ > message2,title2,etc
$ > message3,title3,etc
```

Start consumer console messages from now:
```shell
$ bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic robohorta_new_event
```

Start consumer console from the beginning of saved messages:
```shell
$ bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic robohorta_new_event --from-beginning
```