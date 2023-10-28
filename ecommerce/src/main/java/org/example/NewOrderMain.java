package org.example;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var producer = new KafkaProducer<String, String>(properties());

        for (var idx = 0; idx < 100; idx++) {
            String key = UUID.randomUUID().toString();
            String value = key + ",432,113322";
            var record = new ProducerRecord<String, String>("ecommerce_new_order_2", key, value);
            producer.send(record, getCallback()).get();

            var email = "Thanks! Your order is processing";
            var emailRecord = new ProducerRecord<String, String>("ecommerce_email_new_order_2", key, email);
            producer.send(emailRecord, getCallback()).get();
        }
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

    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }

}
