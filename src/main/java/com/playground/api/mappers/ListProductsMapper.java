package com.playground.api.mappers;

import com.playground.api.dtos.product.ListProductsResponse;
import com.playground.api.models.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListProductsMapper implements RequestMapper<Void, List<ListProductsResponse>, List<Product>> {
    @Override
    public List<ListProductsResponse> modelToResponse(List<Product> products) {
        return products.stream()
                .map(product -> new ListProductsResponse(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getCategory(),
                        product.getStockQuantity(),
                        product.getImageUrl()))
                .toList();
    }
}
