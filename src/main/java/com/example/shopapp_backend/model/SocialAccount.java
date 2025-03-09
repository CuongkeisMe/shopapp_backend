package com.example.shopapp_backend.model;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "social_accounts")

public class SocialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "provider", nullable = false,length = 20)
    private String provider;
    @Column(name = "provider_id", nullable = false,length = 20)
    private String providerId;
    @Column(name = "name", length = 150)
    private String name;
    @Column(name = "email", length = 150)
    private String email;
}
