package com.deliverysystem.delivery.controller;

import com.deliverysystem.delivery.dto.DeliveryResponseDto;
import com.deliverysystem.delivery.response.ApiResponse;
import com.deliverysystem.delivery.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DeliveryResponseDto>> getById(@PathVariable Long id) {
        DeliveryResponseDto delivery = deliveryService.getDeliveryById(id);
        return ResponseEntity.ok(ApiResponse.success("Delivery fetched successfully", delivery));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DeliveryResponseDto>>> getAll() {
        List<DeliveryResponseDto> deliveries = deliveryService.getAllDeliveries();
        return ResponseEntity.ok(ApiResponse.success("All deliveries fetched successfully", deliveries));
    }

    @PostMapping("/mark-delivered/{orderId}")
    public ResponseEntity<String> markDelivered(@PathVariable Long orderId) {
        String response = deliveryService.markAsDelivered(orderId);
        return ResponseEntity.ok(response);
    }
}
