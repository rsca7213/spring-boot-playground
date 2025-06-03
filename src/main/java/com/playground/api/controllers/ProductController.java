package com.playground.api.controllers;

import com.playground.api.dtos.common.PaginationResponse;
import com.playground.api.dtos.product.*;
import com.playground.api.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/products")
@Tag(name = "Products", description = "Endpoints for managing products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(
            summary = "Create a new product",
            description = "Creates a new product with the provided details. Returns the created product."
    )
    @PostMapping
    public ResponseEntity<CreateProductResponse> createProduct(@Valid @RequestBody CreateProductBody requestBody) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(requestBody));
    }

    @Operation(
            summary = "List and paginate products",
            description = "Lists products with pagination support. Returns a paginated response containing the products."
    )
    @GetMapping
    public ResponseEntity<PaginationResponse<ListProductsResponse>> listProducts(@ModelAttribute ListProductsQuery requestQuery) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.listProducts(requestQuery));
    }

    @Operation(
            summary = "Find a product by ID",
            description = "Retrieves a product by its ID. Returns the product details."
    )
    @GetMapping("/{id}")
    public ResponseEntity<FindProductResponse> findProductById(@ModelAttribute FindProductParams requestParams) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findProductById(requestParams.getId()));
    }

    @Operation(
            summary = "Update a product's image",
            description = "Updates an existing product's image. The product ID is provided in the request parameters, and the image file is uploaded as a multipart file. Returns the updated product image details."
    )
    @PutMapping("/{productId}/image")
    public ResponseEntity<UploadProductImageResponse> uploadProductImage(
            @ModelAttribute UploadProductImageParams requestParams,
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.uploadProductImage(requestParams.getProductId(), file));
    }
}
