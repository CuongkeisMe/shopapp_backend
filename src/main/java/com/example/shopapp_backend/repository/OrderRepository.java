package com.example.shopapp_backend.repository;

import com.example.shopapp_backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Tim cac don hang cua 1 user nao do
    List<Order> findByUserId(Long userId);
}
