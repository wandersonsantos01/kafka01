package org.example;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class LogService {

    public static void main(String[] args) throws InterruptedException, IOException {
        LogService logService = new LogService();
        try (KafkaService<String> kafkaService = new KafkaService<>(
                logService.getClass().getSimpleName(),
                Pattern.compile("ecommerce.*"),
                logService::parse,
                String.class,
                Map.of(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName())
        )) {
            kafkaService.run();
        }
    }

    private void parse(ConsumerRecord<String, String> record) {
        System.out.println("LOG");
        System.out.println("topic ::: " + record.topic());
        System.out.println("key ::: " + record.key());
        System.out.println("value ::: " + record.value());
        System.out.println("partition ::: " + record.partition());
        System.out.println("offset ::: " + record.offset());
    }

}
