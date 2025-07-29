package com.deliverysystem.order.service.impl;


import com.deliverysystem.order.client.CustomerServiceFeignClient;
import com.deliverysystem.order.dto.CustomerResponseDto;
import com.deliverysystem.order.dto.OrderRequestDto;
import com.deliverysystem.order.dto.OrderResponseDto;
import com.deliverysystem.order.entity.Order;
import com.deliverysystem.order.enums.OrderStatus;
import com.deliverysystem.order.events.OrderPlacedEvent;
import com.deliverysystem.order.exception.ResourceNotFoundException;
import com.deliverysystem.order.kafka.OrderEventProducer;
import com.deliverysystem.order.repository.OrderRepository;
import com.deliverysystem.order.service.OrderService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final CustomerServiceFeignClient customerClient;
    private final OrderEventProducer orderEventProducer;


    @Override
    public OrderResponseDto placeOrder(OrderRequestDto requestDto) {

        try {
            ResponseEntity<CustomerResponseDto> response = customerClient.getCustomerById(requestDto.getCustomerId());

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new ResourceNotFoundException("Customer not found with ID: " + requestDto.getCustomerId());
            }

        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Customer not found with ID: " + requestDto.getCustomerId());
        } catch (FeignException e) {
            throw new RuntimeException("Failed to validate customer: " + e.getMessage());
        }

        Order order = Order.builder()
                .customerId(requestDto.getCustomerId())
                .productId(requestDto.getProductId())
                .quantity(requestDto.getQuantity())
                .paymentMode(requestDto.getPaymentMode())
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        order = orderRepository.save(order);

        OrderResponseDto responseDto = modelMapper.map(order, OrderResponseDto.class);
        responseDto.setCreatedAt(order.getCreatedAt());

        OrderPlacedEvent event = OrderPlacedEvent.builder()
                .orderId(order.getId())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .paymentMode(order.getPaymentMode())
                .status(order.getStatus())
                .createdAt(LocalDateTime.now())
                .build();

        orderEventProducer.sendOrderCreatedEvent(event);

        return responseDto;
    }

    @Override
    public OrderResponseDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        return modelMapper.map(order, OrderResponseDto.class);
    }
}

