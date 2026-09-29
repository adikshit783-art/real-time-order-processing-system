package com.example.orderApp.service;

import com.example.orderApp.entity.Order;
import com.example.orderApp.entity.OrderItem;
import com.example.orderApp.entity.Product;
import com.example.orderApp.entity.User;
import com.example.orderApp.kafka.OrderProducer;
import com.example.orderApp.repository.OrderRepository;
import com.example.orderApp.repository.ProductRepository;
import com.example.orderApp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderProducer orderProducer;

    public OrderService(
            OrderRepository orderRepository,
            ProductRepository productRepository,
            UserRepository userRepository,
            OrderProducer orderProducer) {

        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderProducer = orderProducer;
    }


    @Transactional
    public Order createOrder(
            Long userId,
            Long productId,
            int quantity) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));

        if (product.getQuantity() < quantity) {
            throw new RuntimeException(
                    "Not enough product stock");
        }

        Order order = new Order();

        order.setUser(user);
        order.setStatus("CREATED");
        order.setCreatedAt(LocalDateTime.now());

        double total = product.getPrice() * quantity;

        order.setTotalAmount(total);

        OrderItem item = new OrderItem();

        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setPrice(product.getPrice());

        order.getOrderItems().add(item);

        product.setQuantity(
                product.getQuantity() - quantity
        );

        productRepository.save(product);

        Order savedOrder = orderRepository.save(order);

        orderProducer.sendOrderCreatedEvent(savedOrder.getId());

        return savedOrder;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}