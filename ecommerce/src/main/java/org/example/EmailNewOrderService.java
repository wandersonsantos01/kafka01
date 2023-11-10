package org.example;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;
import java.util.HashMap;

public class EmailNewOrderService {

    public static void main(String[] args) throws InterruptedException, IOException {
        EmailNewOrderService emailNewOrderService = new EmailNewOrderService();
        try (KafkaService<String> kafkaService = new KafkaService<>(
                EmailNewOrderService.class.getSimpleName(),
                "ecommerce_email_new_order_2",
                emailNewOrderService::parse,
                String.class,
                new HashMap<>()
        )) {
            kafkaService.run();
        }
    }

    private void parse(ConsumerRecord<String, String> record) throws InterruptedException {
        System.out.println("Sending email");
        System.out.println("key ::: " + record.key());
        System.out.println("value ::: " + record.value());
        System.out.println("partition ::: " + record.partition());
        System.out.println("offset ::: " + record.offset());
        Thread.sleep(1000);
        System.out.println("Order checked");
        System.out.println("========================================================");
    }

}
