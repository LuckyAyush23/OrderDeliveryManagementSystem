package com.deliverysystem.delivery.controller;

import com.deliverysystem.delivery.dto.DeliveryPersonRequestDto;
import com.deliverysystem.delivery.dto.DeliveryPersonResponseDto;
import com.deliverysystem.delivery.response.ApiResponse;
import com.deliverysystem.delivery.service.DeliveryPersonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery-persons")
@RequiredArgsConstructor
public class DeliveryPersonController {

    private final DeliveryPersonService deliveryPersonService;

    @PostMapping
    public ResponseEntity<ApiResponse<DeliveryPersonResponseDto>> createDeliveryPerson(
            @Valid @RequestBody DeliveryPersonRequestDto requestDto) {
        DeliveryPersonResponseDto saved = deliveryPersonService.createDeliveryPerson(requestDto);
        return ResponseEntity.ok(ApiResponse.success("Delivery person created successfully", saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DeliveryPersonResponseDto>> getDeliveryPerson(@PathVariable Long id) {
        DeliveryPersonResponseDto dto = deliveryPersonService.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Delivery person fetched", dto));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DeliveryPersonResponseDto>>> getAllDeliveryPersons() {
        List<DeliveryPersonResponseDto> deliveryPersons = deliveryPersonService.getAll();
        return ResponseEntity.ok(ApiResponse.success("All delivery persons fetched", deliveryPersons));
    }
}


