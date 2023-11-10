package org.example;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;
import java.util.HashMap;

public class FraudDetectorService {

    public static void main(String[] args) throws InterruptedException, IOException {
        FraudDetectorService fraudDetectorService = new FraudDetectorService();
        try(KafkaService<Order> kafkaService = new KafkaService<>(
                FraudDetectorService.class.getSimpleName(),
                "ecommerce_new_order_2",
                fraudDetectorService::parse,
                Order.class,
                new HashMap<>()
        )) {
            kafkaService.run();
        }
    }

    private void parse(ConsumerRecord<String, Order> record) throws InterruptedException {
        System.out.println("Checking fraud");
        System.out.println("key ::: " + record.key());
        System.out.println("value ::: " + record.value());
        System.out.println("partition ::: " + record.partition());
        System.out.println("offset ::: " + record.offset());
        Thread.sleep(5000);
        System.out.println("Order checked");
        System.out.println("===============================================================");
    }

}