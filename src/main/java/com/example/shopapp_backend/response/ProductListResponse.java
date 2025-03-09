package com.example.shopapp_backend.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@Builder

public class ProductListResponse {
    private List<ProductResponse> products;
    private int totalPages;
}
