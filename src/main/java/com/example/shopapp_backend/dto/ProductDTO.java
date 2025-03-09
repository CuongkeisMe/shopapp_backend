package com.example.shopapp_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder

public class ProductDTO {
    @NotBlank(message = "name is required")
    @Size(min = 3, max = 200, message = "name must be between 3 and 200 characters")
    private String name;
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    @Max(value = 10000000, message = "Price must be less than or equal to 10,000,000")
    private Float price;
    private String thumbnail;
    private String description;
    @NotNull(message = "category id can not be null")
    @JsonProperty("category_id")
    private Long categoryId;

}
