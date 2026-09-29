package com.example.orderApp.controller;

import com.example.orderApp.entity.Product;
import com.example.orderApp.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String products(Model model) {

        model.addAttribute(
                "products",
                productService.getAllProducts()
        );

        return "products";
    }

    @GetMapping("/new")
    public String newProduct(Model model) {

        model.addAttribute(
                "product",
                new Product()
        );

        return "product-form";
    }

    @PostMapping("/save")
    public String saveProduct(
            @ModelAttribute Product product) {

        productService.saveProduct(product);

        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(
            @PathVariable Long id) {

        productService.deleteProduct(id);

        return "redirect:/products";
    }
}