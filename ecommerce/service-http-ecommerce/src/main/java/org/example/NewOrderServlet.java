package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderServlet extends HttpServlet {

    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();
    private final KafkaDispatcher<String> emailDispatcher = new KafkaDispatcher<>();

    @Override
    public void destroy() {
        super.destroy();
        try {
            orderDispatcher.close();
            emailDispatcher.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        try {
            String email = req.getParameter("email");
            BigDecimal amount = new BigDecimal(req.getParameter("amount"));
            String orderId = UUID.randomUUID().toString();

            Order order = new Order(orderId, amount, email);
            orderDispatcher.send("ecommerce_new_order", email, order);

            var emailMessage = "Thanks! Your order is processing";
            emailDispatcher.send("ecommerce_email_new_order", email, emailMessage);
        } catch (ExecutionException | InterruptedException e) {
            throw new ServletException(e);
        }

        System.out.println("New order sent successfully!");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println("New order sent");

    }
}
