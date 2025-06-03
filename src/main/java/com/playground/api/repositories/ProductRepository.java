package com.playground.api.repositories;

import com.playground.api.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Product findByNameIgnoreCase(String name);
    Page<Product> findAll(Specification<Product> specification, Pageable paginationFilters);
}