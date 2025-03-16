package com.example.shopapp_backend.service;

import com.example.shopapp_backend.dto.OrderDTO;
import com.example.shopapp_backend.exception.DataNotFoundException;
import com.example.shopapp_backend.model.Order;
import com.example.shopapp_backend.model.OrderStatus;
import com.example.shopapp_backend.model.User;
import com.example.shopapp_backend.repository.OrderRepository;
import com.example.shopapp_backend.repository.UserRepository;
import com.example.shopapp_backend.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
//    private final ;

    @Override
    public OrderResponse createOrder(OrderDTO orderDTO) throws Exception {
        // tim xem user id co ton tai hay k
        User existingUser = userRepository
                .findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Can not find user with id " + orderDTO.getUserId()));
        // convert orderDTO -> order
        // dung thu vien Model Mapper
        // tao 1 luong bang xanh xa rieng de kiem soat viec anh xa
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUser(existingUser);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
        // Kiem tra shipping date phai > ngay hom nay
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
        if(shippingDate.isBefore(LocalDate.now())) {
            throw new DataNotFoundException("Shipping date must be at least today !");
        }
        order.setActive(true);
        orderRepository.save(order);
        return modelMapper.map(order, OrderResponse.class);
    }

    @Override
    public Order getOrder(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException{
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find order with id: " + id));
        User existingUser = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Can not find user with id " + orderDTO.getUserId()));
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        modelMapper.map(orderDTO, order);
        order.setUser(existingUser);
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        // order kh dc xoa cung -> xoa mem
        if(order != null) {
            order.setActive(false);
            orderRepository.save(order);
        }
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
