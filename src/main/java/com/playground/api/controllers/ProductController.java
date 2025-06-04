package com.playground.api.controllers;

import com.playground.api.dtos.common.PaginationResponse;
import com.playground.api.dtos.product.*;
import com.playground.api.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreateProductResponse> createProduct(@Valid @RequestBody CreateProductBody requestBody) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(requestBody));
    }

    @Operation(
            summary = "List and paginate products",
            description = "Lists products with pagination support. Returns a paginated response containing the products."
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<PaginationResponse<ListProductsResponse>> listProducts(@ModelAttribute ListProductsQuery requestQuery) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.listProducts(requestQuery));
    }

    @Operation(
            summary = "Find a product by ID",
            description = "Retrieves a product by its ID. Returns the product details."
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<FindProductResponse> findProductById(
            @Parameter(
                    description = "The unique identifier of the product to retrieve",
                    example = "123e4567-e89b-12d3-a456-426614174000",
                    required = true
            ) @PathVariable UUID id
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findProductById(id));
    }

    @Operation(
            summary = "Update a product's image",
            description = "Updates an existing product's image. The product ID is provided in the request parameters, and the image file is uploaded as a multipart file. Returns the updated product image details."
    )
    @PutMapping(value = "/{productId}/image", consumes = "multipart/form-data")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UploadProductImageResponse> uploadProductImage(
            @RequestPart("file") MultipartFile file,
            @Parameter(
                    description = "The product's id to upload the image into",
                    example = "123e4567-e89b-12d3-a456-426614174000",
                    required = true
            ) @PathVariable UUID productId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.uploadProductImage(productId, file));
    }
}
