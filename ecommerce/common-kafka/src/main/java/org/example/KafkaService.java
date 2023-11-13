package org.example;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class KafkaService<T> implements Closeable {
    private final KafkaConsumer<String, T> consumer;
    private final ConsumerFunction parse;
    private final String className;

    public KafkaService(String className, String topic, ConsumerFunction parse, Class<T> type, Map<String, String> properties) {
        this(parse,className, type, properties);
        consumer.subscribe(Collections.singletonList(topic));
    }

    public KafkaService(String className, Pattern topic, ConsumerFunction parse, Class<T> type, Map<String, String> properties) {
        this(parse,className, type, properties);
        consumer.subscribe(topic);
    }

    private KafkaService(ConsumerFunction parse, String className, Class<T> type, Map<String, String> properties) {
        this.parse = parse;
        this.className = className;
        this.consumer = new KafkaConsumer<>(getProperties(type, properties));
    }

    public void run() {
        while (true) {
            var records = consumer.poll(Duration.ofMillis(100));
            if (!records.isEmpty()) {
                System.out.println("Record found");
                for (var record : records) {
                    try {
                        this.parse.consume(record);
                    } catch (InterruptedException e) {
                        // Only log error
                        // throw new RuntimeException(e);
                        System.out.println("Interrupted Exception: " + e.getMessage());
                    } catch (ExecutionException e) {
                        // Only log error
                        // throw new RuntimeException(e);
                        System.out.println("ExecutionException Exception: " + e.getMessage());
                    }
                }
            }
        }
    }

    private Properties getProperties(Class<T> type, Map<String, String> overrideProperties) {
        var properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, this.className);
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, this.className + "_" + UUID.randomUUID().toString());
        properties.setProperty(GsonDeserializer.TYPE_CONFIG, type.getName());
        properties.putAll(overrideProperties);
        return properties;
    }

    @Override
    public void close() throws IOException {
        consumer.close();
    }
}
