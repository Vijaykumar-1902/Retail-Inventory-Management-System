package com.retail.inventory.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.retail.inventory.dto.ProductDTO;
import com.retail.inventory.entity.Product;
import com.retail.inventory.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Add Product
    @PostMapping
    public ResponseEntity<Product> addProduct(
            @Valid @RequestBody Product product) {

        Product savedProduct = productService.addProduct(product);

        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    // Get All Products
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // Get Product By ID
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    // Update Product
    @PutMapping("/{id}")
    public Product updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody Product product) {

        return productService.updateProduct(id, product);
    }

    // Delete Product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }

    // Get Products as DTO
    @GetMapping("/dto")
    public List<ProductDTO> getAllProductsAsDTO() {
        return productService.getAllProductsAsDTO();
    }

    // Pagination and Sorting
    @GetMapping("/page")
    public Page<Product> getProductsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException(
                    "Page must be non-negative and size must be positive");
        }

        Set<String> allowedFields =
                Set.of("id", "name", "price", "quantity");

        if (!allowedFields.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sort field");
        }

        if (!direction.equalsIgnoreCase("asc")
                && !direction.equalsIgnoreCase("desc")) {
            throw new IllegalArgumentException(
                    "Direction must be asc or desc");
        }

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return productService.getProductsWithPagination(pageable);
    }

    // Search Products By Name
    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String name) {
        return productService.searchProductsByName(name);
    }

    // Filter Products By Price
    @GetMapping("/filter")
    public List<Product> filterProductsByPrice(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {

        return productService.filterProductsByPrice(minPrice, maxPrice);
    }

    // Filter Products By Category
    @GetMapping("/category/{categoryId}")
    public List<Product> getProductsByCategory(
            @PathVariable Long categoryId) {

        return productService.getProductsByCategory(categoryId);
    }

    // Increase Stock
    @PutMapping("/{id}/increase-stock")
    public Product increaseStock(
            @PathVariable Long id,
            @RequestParam Integer quantity) {

        return productService.increaseStock(id, quantity);
    }

    // Decrease Stock
    @PutMapping("/{id}/decrease-stock")
    public Product decreaseStock(
            @PathVariable Long id,
            @RequestParam Integer quantity) {

        return productService.decreaseStock(id, quantity);
    }
}