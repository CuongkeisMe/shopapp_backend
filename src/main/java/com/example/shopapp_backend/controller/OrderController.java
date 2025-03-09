package com.example.shopapp_backend.controller;

import com.example.shopapp_backend.dto.OrderDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {

    @PostMapping("")
    public ResponseEntity<?> createOrder(
            @RequestBody @Valid OrderDTO orderDTO,
            BindingResult result) {
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages.toString());
            }
            return ResponseEntity.ok("create order successfully");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<?> getOrdersByUserId(@PathVariable("user_id") Long user_id) {
        try {
            return ResponseEntity.ok("Get orders from User Id successfully");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    // cong viec cua admin
    public ResponseEntity<?> updateOrder(
            @PathVariable("id") Long id,
            @RequestBody @Valid OrderDTO orderDTO) {
        return ResponseEntity.ok("update order successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable("id") Long id) {
        // xoa mem => Cap nhat truong active tu true -> false
        return ResponseEntity.ok("delete order successfully");
    }
}
