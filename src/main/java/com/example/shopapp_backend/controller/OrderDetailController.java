package com.example.shopapp_backend.controller;

import com.example.shopapp_backend.dto.OrderDetailDTO;
import com.example.shopapp_backend.exception.DataNotFoundException;
import com.example.shopapp_backend.model.Order;
import com.example.shopapp_backend.model.OrderDetail;
import com.example.shopapp_backend.repository.OrderDetailRepository;
import com.example.shopapp_backend.repository.OrderRepository;
import com.example.shopapp_backend.response.OrderDetailResponse;
import com.example.shopapp_backend.service.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/order_details")
@RequiredArgsConstructor

public class OrderDetailController {

    private final OrderDetailService orderDetailService;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;

    @PostMapping
    public ResponseEntity<?> createOrderDetail(
            @Valid @RequestBody OrderDetailDTO orderDetailDTO
    ) {
        try {
            OrderDetail newOrderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
            // goi ham convert tu orderDetail -> orderDetailResponse
            return ResponseEntity.ok().body(OrderDetailResponse.fromOrderDetail(newOrderDetail));
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("id") Long id) throws DataNotFoundException {
        OrderDetail orderDetail = orderDetailService.getOrderDetail(id);
        return ResponseEntity.ok().body(OrderDetailResponse.fromOrderDetail(orderDetail));
//        return ResponseEntity.ok(orderDetail);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("orderId") Long orderId) {
        List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderId);
        List<OrderDetailResponse> orderDetailResponses = orderDetails.stream().map(OrderDetailResponse::fromOrderDetail).toList();
        return ResponseEntity.ok(orderDetailResponses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(
            @Valid @PathVariable("id") Long id,
            @RequestBody OrderDetailDTO orderDetailDTO
    ) throws DataNotFoundException {
        try {
            OrderDetail orderDetail = orderDetailService.updateOrderDetail(id, orderDetailDTO);
            return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(orderDetail));
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(@Valid @PathVariable("id") Long id) {
        orderDetailService.deleteById(id);
        return ResponseEntity.ok("delete order detail successfully with id: " + id);
    }

}
