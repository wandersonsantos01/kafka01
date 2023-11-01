package org.example;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

public class KafkaService {
    private final KafkaConsumer<String, String> consumer;
    private final ConsumerFunction parse;
    private final String className;

    public KafkaService(String className, String topic, ConsumerFunction parse) throws InterruptedException {
        this.className = className;
        this.parse = parse;
        this.consumer = new KafkaConsumer<>(properties());
        consumer.subscribe(Collections.singletonList(topic));

    }

    public void run() throws InterruptedException {
        while (true) {
            var records = consumer.poll(Duration.ofMillis(100));
            if (!records.isEmpty()) {
                System.out.println("Record found");
                for (var record : records) {
                    this.parse.consume(record);
                }
            }
        }
    }

    private Properties properties() {
        var properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, this.className);
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, this.className + "_" + UUID.randomUUID().toString());
        return properties;
    }

}
