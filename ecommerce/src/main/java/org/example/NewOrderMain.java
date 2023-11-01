package org.example;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        try (KafkaDispatcher dispatcher = new KafkaDispatcher()) {
            for (var idx = 0; idx < 10; idx++) {
                String key = UUID.randomUUID().toString();
                String value = key + ",432,113322";
                dispatcher.send("ecommerce_new_order_2", key, value);

                var email = "Thanks! Your order is processing";
                dispatcher.send("ecommerce_email_new_order_2", key, email);
            }
        }
    }

}
