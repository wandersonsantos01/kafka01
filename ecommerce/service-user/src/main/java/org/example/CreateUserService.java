package org.example;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;

public class CreateUserService {

    private final Connection connection;

    public CreateUserService() throws SQLException {
        String url = "jdbc:sqlite:ecommerce/service-user/target/user_database.db";
        this.connection = DriverManager.getConnection(url);
        connection.createStatement().execute("CREATE TABLE users (" +
                    "uuid VARCHAR(200) PRIMARY KEY," +
                    "email VARCHAR(200)" +
                ")");
    }

    public static void main(String[] args) throws IOException, SQLException {
        CreateUserService fraudDetectorService = new CreateUserService();
        try(KafkaService<Order> kafkaService = new KafkaService<>(
                CreateUserService.class.getSimpleName(),
                "ecommerce_new_order",
                fraudDetectorService::parse,
                Order.class,
                new HashMap<>()
        )) {
            kafkaService.run();
        }
    }

    private void parse(ConsumerRecord<String, Order> record) throws SQLException {
        System.out.println("Checking for new user");
        System.out.println("value ::: " + record.value());

        Order order = record.value();
        if (isNewUser(order.getEmail())) {
            insertNewUser(order.getEmail());
        }

        System.out.println("===============================================================");
    }

    private void insertNewUser(String email) throws SQLException {
        PreparedStatement insert = connection.prepareStatement("INSERT INTO users " +
                "(uuid, email) " +
                "VALUES " +
                "(?, ?)");
        insert.setString(1, "uuid");
        insert.setString(2, email);
        insert.execute();
        System.out.println("User UUID e email " + email + " inserted");
    }

    private boolean isNewUser(String email) throws SQLException {
        PreparedStatement check = connection.prepareStatement("SELECT uuid FROM users WHERE email = ? LIMIT 1");
        check.setString(1, email);
        ResultSet results = check.executeQuery();
        return !results.next();
    }

}
