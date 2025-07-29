package com.deliverysystem.order.service;


import com.deliverysystem.order.dto.OrderRequestDto;
import com.deliverysystem.order.dto.OrderResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    OrderResponseDto placeOrder(OrderRequestDto orderRequestDto);
    OrderResponseDto getOrderById(Long orderId);
}

