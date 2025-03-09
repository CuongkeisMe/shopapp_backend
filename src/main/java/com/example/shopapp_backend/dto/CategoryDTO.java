package com.example.shopapp_backend.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class CategoryDTO {
    @NotEmpty(message = "Category's name cannot be empty")
    private String name;
}
