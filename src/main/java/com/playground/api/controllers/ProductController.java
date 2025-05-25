package com.playground.api.controllers;

import com.playground.api.dtos.common.PaginationQuery;
import com.playground.api.dtos.common.PaginationResponse;
import com.playground.api.dtos.product.CreateProductBody;
import com.playground.api.dtos.product.CreateProductResponse;
import com.playground.api.dtos.product.ListProductsResponse;
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
    public ResponseEntity<CreateProductResponse> createProduct(@Valid @RequestBody CreateProductBody requestBody) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(requestBody));
    }

    @GetMapping
    public ResponseEntity<PaginationResponse<ListProductsResponse>> listProducts(@ModelAttribute PaginationQuery pagination) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.listProducts(pagination));
    }
}
