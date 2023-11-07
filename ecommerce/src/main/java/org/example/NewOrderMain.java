package org.example;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        String userId = UUID.randomUUID().toString();
        try (KafkaDispatcher orderDispatcher = new KafkaDispatcher<Order>()) {
            for (var idx = 0; idx < 10; idx++) {
                String orderId = UUID.randomUUID().toString();
                BigDecimal amount = new BigDecimal(Math.random() * 5000 + 1);
                Order order = new Order(userId, orderId, amount);
                orderDispatcher.send("ecommerce_new_order_2", userId, order);
            }
        }

        try (KafkaDispatcher emailDispatcher = new KafkaDispatcher<String>()) {
            for (var idx = 0; idx < 10; idx++) {
                var email = "Thanks! Your order is processing";
                emailDispatcher.send("ecommerce_email_new_order_2", userId, email);
            }
        }
    }

}
