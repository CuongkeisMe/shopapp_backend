package com.example.shopapp_backend.dto;

import com.example.shopapp_backend.model.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ProductImageDTO {
    @JsonProperty("product_id")
    @Min(value = 1, message = "Product's id must be > 0")
    private Long productId;
    @Size(min = 5, max = 255, message = "Image's name must > 0 and < 255 characters")
    @JsonProperty("image_url")
    private String imageURL;
}
