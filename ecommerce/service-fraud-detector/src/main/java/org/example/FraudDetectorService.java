package org.example;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class FraudDetectorService {

    public static void main(String[] args) throws InterruptedException, IOException {
        FraudDetectorService fraudDetectorService = new FraudDetectorService();
        try(KafkaService<Order> kafkaService = new KafkaService<>(
                FraudDetectorService.class.getSimpleName(),
                "ecommerce_new_order",
                fraudDetectorService::parse,
                Order.class,
                new HashMap<>()
        )) {
            kafkaService.run();
        }
    }

    private final KafkaDispatcher<Order> orderKafkaDispatcher = new KafkaDispatcher<>();

    private void parse(ConsumerRecord<String, Order> record) throws InterruptedException, ExecutionException {
        System.out.println("Checking fraud");
        System.out.println("key ::: " + record.key());
        System.out.println("value ::: " + record.value());
        System.out.println("partition ::: " + record.partition());
        System.out.println("offset ::: " + record.offset());
        Thread.sleep(2000);

        Order order = record.value();
        if (isFraud(order)) {
            System.out.println("Order is a fraud !!!" + order);
            orderKafkaDispatcher.send(
                    "ecommerce_order_rejected",
                    order.getUserId(),
                    order
            );
        } else {
            System.out.println("Order checked");
            orderKafkaDispatcher.send(
                    "ecommerce_order_accepted",
                    order.getUserId(),
                    order
            );
        }
        System.out.println("===============================================================");
    }

    private static boolean isFraud(Order order) {
        return order.getAmount().compareTo(new BigDecimal("2500")) >= 0;
    }

}
