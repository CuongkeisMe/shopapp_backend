package com.example.shopapp_backend.service;

import com.example.shopapp_backend.dto.ProductDTO;
import com.example.shopapp_backend.dto.ProductImageDTO;
import com.example.shopapp_backend.exception.DataNotFoundException;
import com.example.shopapp_backend.exception.InvalidParamException;
import com.example.shopapp_backend.model.Category;
import com.example.shopapp_backend.model.Product;
import com.example.shopapp_backend.model.ProductImage;
import com.example.shopapp_backend.repository.CategoryRepository;
import com.example.shopapp_backend.repository.ProductImageRepository;
import com.example.shopapp_backend.repository.ProductRepository;
import com.example.shopapp_backend.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        Category existCategory = categoryRepository.findById(Long.valueOf(productDTO.getCategoryId()))
                .orElseThrow(() -> new DataNotFoundException("Can not find category id with: " + productDTO.getCategoryId()));
        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .description(productDTO.getDescription())
                .category(existCategory)
                .build();
        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(Long id) throws Exception {
        return productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Can not find product with id: " + id));
    }

    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
        // Lay danh sach san pham theo trang(page) va limit(gioi han moi trang)
        return productRepository
                .findAll(pageRequest)
                .map(ProductResponse::fromProduct);
    }

    @Override
    public Product updateProduct(Long id, ProductDTO productDTO) throws Exception {
        Product existingProduct = getProductById(id);
        if (existingProduct != null) {
            // copy cac thuoc tinh tu dto -> object
            // su dung model mapper
            existingProduct.setName(productDTO.getName());
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setThumbnail(productDTO.getThumbnail());
            // Lay ra dc category sua va set vao product
            Category existCategory = categoryRepository.findById(Long.valueOf(productDTO.getCategoryId()))
                    .orElseThrow(() -> new DataNotFoundException("Can not find category id with: " + productDTO.getCategoryId()));
            existingProduct.setCategory(existCategory);
            existingProduct.setDescription(productDTO.getDescription());
            return productRepository.save(existingProduct);
        }
        return null;
    }

    @Override
    public void deleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            productRepository.delete(optionalProduct.get());
        }
    }

    @Override
    public boolean existByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws Exception {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Can not find product with id: " + productImageDTO.getProductId()));
        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageURL(productImageDTO.getImageURL())
                .build();
        // K cho insert qua 5 anh cho 1 sp
        int size = productImageRepository.findByProductId(productId).size();
        if (size >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
            throw new InvalidParamException("Number of images must be <= 5!");
        }
        return productImageRepository.save(newProductImage);
    }
}
