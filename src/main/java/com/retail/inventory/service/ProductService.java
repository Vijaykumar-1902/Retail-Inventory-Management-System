package com.retail.inventory.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.retail.inventory.dto.ProductDTO;
import com.retail.inventory.dto.ProductMapper;
import com.retail.inventory.entity.Category;
import com.retail.inventory.entity.Product;
import com.retail.inventory.exception.ResourceNotFoundException;
import com.retail.inventory.repository.CategoryRepository;
import com.retail.inventory.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository) {

        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    // Add Product
    public Product addProduct(Product product) {

        if (product.getCategory() == null
                || product.getCategory().getId() == null) {
            throw new IllegalArgumentException("Category ID is required");
        }

        Category category = categoryRepository.findById(
                product.getCategory().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found"));

        product.setCategory(category);

        return productRepository.save(product);
    }

    // Get All Products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Get Product By ID
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with ID: " + id));
    }

    // Update Product
    public Product updateProduct(Long id, Product updatedProduct) {

        Product existingProduct = getProductById(id);

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setQuantity(updatedProduct.getQuantity());

        if (updatedProduct.getCategory() != null) {

            Long categoryId = updatedProduct.getCategory().getId();

            if (categoryId == null) {
                throw new IllegalArgumentException(
                        "Category ID is required");
            }

            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Category not found with ID: " + categoryId));

            existingProduct.setCategory(category);
        }

        return productRepository.save(existingProduct);
    }

    // Delete Product
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }

    // Product DTO
    public List<ProductDTO> getAllProductsAsDTO() {
        return productRepository.findAll()
                .stream()
                .map(ProductMapper::toDTO)
                .toList();
    }

    // Pagination
    public Page<Product> getProductsWithPagination(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    // Search Products By Name
    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    // Filter Products By Price
    public List<Product> filterProductsByPrice(
            BigDecimal minPrice, BigDecimal maxPrice) {

        if (minPrice == null || maxPrice == null
                || minPrice.compareTo(BigDecimal.ZERO) < 0
                || maxPrice.compareTo(minPrice) < 0) {

            throw new IllegalArgumentException("Invalid price range");
        }

        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    // Filter Products By Category
    public List<Product> getProductsByCategory(Long categoryId) {

        if (categoryId == null
                || !categoryRepository.existsById(categoryId)) {

            throw new ResourceNotFoundException(
                    "Category not found with ID: " + categoryId);
        }

        return productRepository.findByCategoryId(categoryId);
    }

    // Increase Stock
    @Transactional
    public Product increaseStock(Long productId, Integer quantity) {

        Product product = getProductById(productId);

        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero");
        }

        product.setQuantity(product.getQuantity() + quantity);

        return productRepository.save(product);
    }

    // Decrease Stock
    @Transactional
    public Product decreaseStock(Long productId, Integer quantity) {

        Product product = getProductById(productId);

        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero");
        }

        if (product.getQuantity() < quantity) {
            throw new IllegalArgumentException(
                    "Insufficient stock available");
        }

        product.setQuantity(product.getQuantity() - quantity);

        return productRepository.save(product);
    }
}