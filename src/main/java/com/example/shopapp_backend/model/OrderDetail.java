package com.example.shopapp_backend.model;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "order_details")

public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private Float price;
    @Column(name = "number_of_products", nullable = false)
    private Integer numberOfProducts;
    @Column(name = "total_money", nullable = false)
    private Float totalPrice;
    @Column(name = "color")
    private String color;
}
