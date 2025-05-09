package com.example.shopapp_backend.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder

public class OrderResponse extends BaseResponse{
    private Long id;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("fullname")
    private String fullName;
    @JsonProperty("phone_number")
    private String phoneNumber;
    private String address;
    private String note;
    @JsonProperty("total_money")
    private Float totalMoney;
    @JsonProperty("order_date")
    private Date orderDate;
    private String status;
    @JsonProperty("shipping_method")
    private String shippingMethod;
    @JsonProperty("shipping_address")
    private String shippingAddress;
    @JsonProperty("shipping_date")
    private LocalDate shippingDate;
    @JsonProperty("tracking_number")
    private String trackingNumber;
    @JsonProperty("payment_method")
    private String paymentMethod;
    @JsonProperty("is_active")
    private Boolean isActive;
}
