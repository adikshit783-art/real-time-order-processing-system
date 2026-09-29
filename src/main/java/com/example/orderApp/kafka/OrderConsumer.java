package com.example.orderApp.kafka;

import com.example.orderApp.entity.Order;
import com.example.orderApp.repository.OrderRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    private final OrderRepository orderRepository;

    public OrderConsumer(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @KafkaListener(
            topics = "order-created",
            groupId = "order-group"
    )
    public void consumeOrderCreatedEvent(String message) {

        System.out.println("Received order event: " + message);

        Long orderId = Long.parseLong(message);

        Order order = orderRepository
                .findById(orderId)
                .orElse(null);

        if (order == null) {
            System.out.println("Order not found: " + orderId);
            return;
        }

        order.setStatus("PROCESSING");

        orderRepository.save(order);

        System.out.println(
                "Order " + orderId +
                        " status updated to PROCESSING"
        );
    }
}