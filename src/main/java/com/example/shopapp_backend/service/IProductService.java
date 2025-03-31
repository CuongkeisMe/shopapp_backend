package com.example.shopapp_backend.service;

import com.example.shopapp_backend.dto.ProductDTO;
import com.example.shopapp_backend.dto.ProductImageDTO;
import com.example.shopapp_backend.exception.DataNotFoundException;
import com.example.shopapp_backend.model.Product;
import com.example.shopapp_backend.model.ProductImage;
import com.example.shopapp_backend.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws DataNotFoundException;
    Product getProductById(Long id) throws Exception;
    Page<ProductResponse> getAllProducts(Long categoryId, String keyword, PageRequest pageRequest);
    Product updateProduct(Long id, ProductDTO productDTO) throws Exception;
    void deleteProduct(Long id);
    boolean existByName(String name);
    ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws Exception;
}
