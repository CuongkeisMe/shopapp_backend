package com.example.shopapp_backend.service;

import com.example.shopapp_backend.dto.OrderDetailDTO;
import com.example.shopapp_backend.exception.DataNotFoundException;
import com.example.shopapp_backend.model.Order;
import com.example.shopapp_backend.model.OrderDetail;
import com.example.shopapp_backend.model.Product;
import com.example.shopapp_backend.repository.OrderDetailRepository;
import com.example.shopapp_backend.repository.OrderRepository;
import com.example.shopapp_backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public OrderDetail createOrderDetail(OrderDetailDTO newOrderDetail) throws Exception {
        // Tim xem order id co ton tai kh
        Order oder = orderRepository.findById(newOrderDetail.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find order with id: " + newOrderDetail.getOrderId()));
        // Tim product theo id
        Product product = productRepository.findById(newOrderDetail.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find product with id: " + newOrderDetail.getProductId()));
        OrderDetail orderDetail = OrderDetail.builder()
                .order(oder)
                .product(product)
                .price(newOrderDetail.getPrice())
                .numberOfProducts(newOrderDetail.getNumberOfProducts())
                .totalPrice(newOrderDetail.getPrice())
                .color(newOrderDetail.getColor())
                .build();
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public OrderDetail getOrderDetail(Long id) throws DataNotFoundException {
        return orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find order detail with id: " + id));
    }

    @Override
    @Transactional
    public OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws Exception {
        // tim xem order detail co ton tai kh
        OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find order detail with id: " + id));
        Order existingOrder = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find order with id: " + orderDetailDTO.getOrderId()));
        Product existingProduct = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find product with id: " + orderDetailDTO.getProductId()));
        existingOrderDetail.setPrice(orderDetailDTO.getPrice());
        existingOrderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());
        existingOrderDetail.setColor(orderDetailDTO.getColor());
        existingOrderDetail.setTotalPrice(orderDetailDTO.getPrice());
        existingOrderDetail.setOrder(existingOrder);
        existingOrderDetail.setProduct(existingProduct);
        return orderDetailRepository.save(existingOrderDetail);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
}
