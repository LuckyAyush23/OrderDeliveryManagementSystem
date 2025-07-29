package com.deliverysystem.order.controller;

import com.deliverysystem.order.dto.OrderRequestDto;
import com.deliverysystem.order.dto.OrderResponseDto;
import com.deliverysystem.order.response.ApiResponse;
import com.deliverysystem.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponseDto>> createOrder(@Valid @RequestBody OrderRequestDto orderRequestDto) {
        OrderResponseDto responseDto = orderService.placeOrder(orderRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Order placed successfully", responseDto));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> getOrder(@PathVariable Long orderId) {
        OrderResponseDto order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(ApiResponse.success("Order fetched successfully", order));
    }
}

