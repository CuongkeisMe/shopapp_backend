package com.example.shopapp_backend.service;

import com.example.shopapp_backend.dto.CategoryDTO;
import com.example.shopapp_backend.model.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTO);
    Category getCategoryById(Long id);
    List<Category> getAllCategories();
    Category updateCategory(Long id, CategoryDTO categoryDTO);
    void deleteCategory(Long id);
}
