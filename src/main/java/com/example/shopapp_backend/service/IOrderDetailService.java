package com.example.shopapp_backend.service;

import com.example.shopapp_backend.dto.OrderDetailDTO;
import com.example.shopapp_backend.exception.DataNotFoundException;
import com.example.shopapp_backend.model.OrderDetail;

import java.util.List;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO newOrderDetail) throws Exception;
    OrderDetail getOrderDetail(Long id) throws DataNotFoundException;
    OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws Exception;
    void deleteById(Long id);
    List<OrderDetail> findByOrderId(Long orderId);
}
