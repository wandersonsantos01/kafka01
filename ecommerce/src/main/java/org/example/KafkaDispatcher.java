package org.example;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaDispatcher implements Closeable {

    private final KafkaProducer<String, String> producer;

    KafkaDispatcher() {
        this.producer = new KafkaProducer<String, String>(properties());
    }

    private static Properties properties() {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());
        return properties;
    }

    public void send(String topic, String key, String value) throws ExecutionException, InterruptedException {
        var record = new ProducerRecord<String, String>(topic, key, value);
        producer.send(record, getCallback()).get();

        var email = "Thanks! Your order is processing";
        var emailRecord = new ProducerRecord<String, String>("ecommerce_email_new_order_2", key, email);
        producer.send(emailRecord, getCallback()).get();
    }

    private static Callback getCallback() {
        return (data, exeption) -> {
            if (exeption != null) {
                exeption.printStackTrace();
                return;
            }
            System.out.println("Success - topic " + data.topic() + "::: partition " + data.partition() + "/ offset " + data.offset() + "/ timestamp" + data.timestamp());
        };
    }

    @Override
    public void close() throws IOException {
        producer.close();
    }

}
