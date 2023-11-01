package org.example;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class LogService {

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        LogService logService = new LogService();
        try (KafkaService kafkaService = new KafkaService(
                logService.getClass().getSimpleName(),
                "ecommerce.*",
                logService::parse
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
