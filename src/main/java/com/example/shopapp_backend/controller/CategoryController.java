package com.example.shopapp_backend.controller;

import com.example.shopapp_backend.component.LocalizationUtil;
import com.example.shopapp_backend.dto.CategoryDTO;
import com.example.shopapp_backend.model.Category;
import com.example.shopapp_backend.service.CategoryService;
import com.example.shopapp_backend.util.MessageKey;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final LocalizationUtil localizationUtil;

    @PostMapping("")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO categoryDTO,
                                            BindingResult result
    ) {
        if(result.hasErrors()){
            List<String> errormessage = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errormessage);
        }
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok().body(localizationUtil.getLocalizedMessage(MessageKey.INSERT_CATEGORY_SUCCESSFULLY));
    }

    // Hien thi tat ca cac category
    @GetMapping("")
    public ResponseEntity<List<Category>> getAllCategories(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }



    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id,
                                                 @Valid @RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok("Update category successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().body(localizationUtil.getLocalizedMessage(MessageKey.DELETE_CATEGORY_SUCCESSFULLY));
    }
}
