package com.example.shopapp_backend.service;

import com.example.shopapp_backend.dto.OrderDTO;
import com.example.shopapp_backend.exception.DataNotFoundException;
import com.example.shopapp_backend.model.User;
import com.example.shopapp_backend.repository.OrderRepository;
import com.example.shopapp_backend.repository.UserRepository;
import com.example.shopapp_backend.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public OrderResponse createOrder(OrderDTO orderDTO) throws Exception {
        // tim xem user id co ton tai hay k
        User existingUser = userRepository
                .findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Can not find user with id " + orderDTO.getUserId()));
        // convert orderDTO -> order

        return null;
    }

    @Override
    public OrderResponse getOrder(Long id) {
        return null;
    }

    @Override
    public OrderResponse updateOrder(Long id, OrderDTO orderDTO) {
        return null;
    }

    @Override
    public void deleteOrder(Long id) {

    }

    @Override
    public List<OrderResponse> getAllOrders(Long userId) {
        return List.of();
    }
}
