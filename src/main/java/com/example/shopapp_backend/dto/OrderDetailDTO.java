package com.example.shopapp_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class OrderDetailDTO {
    @JsonProperty("order_id")
    private Long orderId;
    @JsonProperty("product_id")
    private Long productId;
    private Float price;
    @JsonProperty("number_of_products")
    private int numberOfProducts;
    @JsonProperty("total_money")
    private Float totalMoney;
    private String color;
}
