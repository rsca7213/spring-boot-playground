package com.playground.api.controllers;

import com.playground.api.dtos.product.CreateProductRequestBodyDto;
import com.playground.api.dtos.product.CreateProductResponseBodyDto;
import com.playground.api.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<CreateProductResponseBodyDto> createProduct(@Valid @RequestBody CreateProductRequestBodyDto requestBody) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.createProduct(requestBody));
    }
}
