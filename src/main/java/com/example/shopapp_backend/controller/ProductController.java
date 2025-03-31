package com.example.shopapp_backend.controller;

import com.example.shopapp_backend.component.LocalizationUtil;
import com.example.shopapp_backend.dto.ProductDTO;
import com.example.shopapp_backend.dto.ProductImageDTO;
import com.example.shopapp_backend.exception.DataNotFoundException;
import com.example.shopapp_backend.model.Product;
import com.example.shopapp_backend.model.ProductImage;
import com.example.shopapp_backend.response.ProductListResponse;
import com.example.shopapp_backend.response.ProductResponse;
import com.example.shopapp_backend.service.ProductService;
import com.example.shopapp_backend.util.MessageKey;
import com.github.javafaker.Faker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final LocalizationUtil localizationUtil;

    @GetMapping("")
    public ResponseEntity<ProductListResponse> getProduct(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0", name = "category_id") Long categoryId,
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("id").ascending());
        Page<ProductResponse> productPage = productService.getAllProducts(categoryId, keyword, pageRequest);
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();
        return ResponseEntity.ok(ProductListResponse
                .builder()
                .products(products)
                .totalPages(totalPages)
                .build());
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long productId) {
        try {
            Product existingProduct = productService.getProductById(productId);
            return ResponseEntity.ok(ProductResponse.fromProduct(existingProduct));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") Long productId) {
        try {
            productService.deleteProduct(productId);
            return ResponseEntity.ok(String.format("Product with id = %d deleted successfully", productId));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDTO productDTO,
                                            BindingResult result
    ) throws IOException, DataNotFoundException {
        try {
            if(result.hasErrors()){
                List<String> errormessage = result.getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errormessage);
            }
            Product newProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok(newProduct);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(@PathVariable("id") Long productId,
                                          @ModelAttribute("files") MultipartFile[] files) {
        try {
            // Lay ra product de them anh
            Product existingProduct = productService.getProductById(productId);
            if (files.length > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT){
                return ResponseEntity.badRequest().body(localizationUtil.getLocalizedMessage(MessageKey.UPLOAD_IMAGES_MAX_5));
            }
            // khoi tao list anh de add nhieu anh cung luc
            List<ProductImage> productImages = new ArrayList<>();
            // validate anh
            for (MultipartFile file : files) {
                if (file.getSize() > 10 * 1024 * 1024) { // kich thuoc lon hon 10 mb
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body(localizationUtil.getLocalizedMessage(MessageKey.UPLOAD_IMAGES_FILE_LARGE));
                }
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body(localizationUtil.getLocalizedMessage(MessageKey.UPLOAD_IMAGES_FILE_MUST_BE_IMAGE));
                }
                String fileName = this.storeFile(file);
                ProductImage productImage = productService.createProductImage(existingProduct.getId(),
                        ProductImageDTO.builder()
                                .imageURL(fileName)
                                .build()
                );
                productImages.add(productImage);
            }
            return ResponseEntity.ok().body(productImages);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable("imageName") String imageName) {
        try {
            Path imagePath = Paths.get("uploads/" + imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());
            if(resource.exists()){
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            }else {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(new UrlResource(Paths.get("uploads/404.jpg").toUri().toString()));
//                return ResponseEntity.notFound().build();
            }
        }catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    private String storeFile(MultipartFile file) throws IOException {
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("Invalid image format");
        }
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        // them UUID vao trc ten file de dam bao ten file la duy nhat
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        // Duong dan den thu muc ma ban muon luu file
        Path uploadDir = Paths.get("uploads");
        // Kiem tra va tao thu muc neu no kh ton tai
        if(!Files.exists(uploadDir)){
            Files.createDirectory(uploadDir);
        }
        // Duong dan day du den file
        Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        // Sao chep file vao thu muc dich
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    public boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    // Ham tu generate product, luc nao dung thi bat
//    @PostMapping("/generateFakeProducts")
    public ResponseEntity<String> generateFakeProducts() {
        Faker faker = new Faker();
        for(int i = 0; i < 200; i++){
            String productName = faker.commerce().productName();
            if(productService.existByName(productName)){
                continue;
            }
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price((float) faker.number().numberBetween(10, 90_000_000))
                    .thumbnail("")
                    .description(faker.lorem().sentence())
                    .categoryId((long) faker.number().numberBetween(1,5))
                    .build();
            try {
                productService.createProduct(productDTO);
            }catch (Exception e){
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("Fake products created successfully!");
    }

}
