package com.example.orderApp.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

    private static final String TOPIC = "order-created";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderCreatedEvent(Long orderId) {

        String message = String.valueOf(orderId);

        kafkaTemplate.send(TOPIC, message);

        System.out.println(
                "Order event sent to Kafka: " + orderId
        );
    }
}