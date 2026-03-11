package com.shopwave.product.service;

import com.shopwave.product.model.Product;
import com.shopwave.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getProducts(String category, String search,
            BigDecimal minPrice, BigDecimal maxPrice) {
        if (search != null && !search.isBlank()) {
            return productRepository.findByNameContainingIgnoreCaseAndActiveTrue(search);
        }
        if (category != null && !category.isBlank() && minPrice != null && maxPrice != null) {
            return productRepository.findByCategoryAndMaxPrice(category, maxPrice)
                .stream().filter(p -> p.getPrice().compareTo(minPrice) >= 0).toList();
        }
        if (category != null && !category.isBlank()) {
            return productRepository.findByCategoryIgnoreCaseAndActiveTrue(category);
        }
        return productRepository.findAll().stream().filter(Product::isActive).toList();
    }

    public Product getById(String id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found: " + id));
    }

    public Product create(Product product) {
        return productRepository.save(product);
    }

    public Product update(String id, Product updated) {
        Product existing = getById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setCategory(updated.getCategory());
        existing.setPrice(updated.getPrice());
        existing.setStockQuantity(updated.getStockQuantity());
        existing.setBrand(updated.getBrand());
        existing.setSku(updated.getSku());
        existing.setImageUrls(updated.getImageUrls());
        existing.setAttributes(updated.getAttributes());
        return productRepository.save(existing);
    }

    public void softDelete(String id) {
        Product product = getById(id);
        product.setActive(false);
        productRepository.save(product);
    }

    public void updateStock(String id, int quantity) {
        Product product = getById(id);
        if (product.getStockQuantity() < quantity) {
            throw new IllegalStateException("Insufficient stock for: " + product.getName());
        }
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);
    }

    public List<Product> getLowStock(int threshold) {
        return productRepository.findLowStockProducts(threshold);
    }
}
