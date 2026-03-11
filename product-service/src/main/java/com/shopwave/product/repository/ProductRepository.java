package com.shopwave.product.repository;

import com.shopwave.product.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    List<Product> findByCategoryIgnoreCaseAndActiveTrue(String category);

    List<Product> findByNameContainingIgnoreCaseAndActiveTrue(String name);

    List<Product> findByPriceBetweenAndActiveTrue(BigDecimal minPrice, BigDecimal maxPrice);

    @Query("{ 'category': ?0, 'price': { $lte: ?1 }, 'active': true }")
    List<Product> findByCategoryAndMaxPrice(String category, BigDecimal maxPrice);

    List<Product> findByBrandIgnoreCaseAndActiveTrue(String brand);

    @Query("{ 'stockQuantity': { $lte: ?0 }, 'active': true }")
    List<Product> findLowStockProducts(int threshold);

    long countByCategoryIgnoreCaseAndActiveTrue(String category);
}
