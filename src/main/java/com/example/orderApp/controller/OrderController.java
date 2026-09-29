package com.example.orderApp.controller;

import com.example.orderApp.entity.Order;
import com.example.orderApp.service.OrderService;
import com.example.orderApp.service.ProductService;
import com.example.orderApp.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final ProductService productService;
    private final UserRepository userRepository;

    public OrderController(
            OrderService orderService,
            ProductService productService,
            UserRepository userRepository) {

        this.orderService = orderService;
        this.productService = productService;
        this.userRepository = userRepository;
    }


    @GetMapping
    public String orders(Model model) {

        model.addAttribute(
                "orders",
                orderService.getAllOrders()
        );

        return "orders";
    }


    @GetMapping("/new")
    public String newOrder(Model model) {

        model.addAttribute(
                "users",
                userRepository.findAll()
        );

        model.addAttribute(
                "products",
                productService.getAllProducts()
        );

        return "order";
    }


    @PostMapping("/create")
    public String createOrder(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam int quantity) {

        orderService.createOrder(
                userId,
                productId,
                quantity
        );

        return "redirect:/orders";
    }
}