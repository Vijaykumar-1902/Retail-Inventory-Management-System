package com.retail.inventory.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.retail.inventory.entity.Category;
import com.retail.inventory.exception.ResourceNotFoundException;
import com.retail.inventory.repository.CategoryRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Add Category
    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    // Get All Categories
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // Get Category By ID
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with ID: " + id));
    }

    // Update Category
    public Category updateCategory(Long id, Category updatedCategory) {

        Category existingCategory = getCategoryById(id);

        existingCategory.setName(updatedCategory.getName());
        existingCategory.setDescription(updatedCategory.getDescription());

        return categoryRepository.save(existingCategory);
    }

    // Delete Category
    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        categoryRepository.delete(category);
    }
}