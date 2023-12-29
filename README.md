# kafka01

## Commands

Start Zookeeper Server - Where the messages are saved:

```shell
bin/zookeeper-server-start.sh config/zookeeper.properties
```

Start Kafka Server:
```shell
bin/kafka-server-start.sh config/server.properties
```

Create new topic:
```shell
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic ecommerce_new_order
```

Start producer console:
```shell
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic ecommerce_new_order

$ > message1,title1,etc
$ > message2,title2,etc
$ > message3,title3,etc
```

Start consumer console messages from now:
```shell
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic ecommerce_new_order
```

Start consumer console from the beginning of saved messages:
```shell
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic ecommerce_new_order --from-beginning

Show topics:
```shell
bin/kafka-topics.sh --list --bootstrap-server localhost:9092
```

Delete topic:
```shell
bin/kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic ecommerce_email_new_order
```

Describe groups:
```shell
bin/kafka-consumer-groups.sh --all-groups --bootstrap-server localhost:9092 --describe
```

Describe topics:
```shell
bin/kafka-topics.sh --describe --bootstrap-server localhost:9092
```

Create another kafka broker:

1- Duplicate file `config/server.properties` to `config/server2.properties`, for example:
```shell
cp config/server.properties config/server2.properties
```

2- Open `config/server2.properties`:

2.1- Change lines:
```shell
broker.id=0 -> broker.id=2
log.dirs=/home/wanderson/kafka/kafka_2.13-3.5.1/data/kafka2
```

2.2- Include line after broker.id:
```shell
default.replication.factor=3
offsets.topic.replication.factor=3
transaction.state.log.replication.factor=3
```

3- Open `config/server.properties`:

3.1- Include line after broker.id:
```shell
default.replication.factor=3
```

4- Stop all consumers, Zoopeeker and Kafka servers

5- Clean Zookeeper and Kafka files:
```shell
rm -rf data/zookeper/*
rm -rf data/kafka/*
```

6- Start all consumers, Zookeeper and Kafka servers

6.1- New broker:
```shell
bin/kafka-server-start.sh config/server2.properties
```


