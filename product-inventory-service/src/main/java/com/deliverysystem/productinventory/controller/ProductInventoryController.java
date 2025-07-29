package com.deliverysystem.productinventory.controller;

import com.deliverysystem.productinventory.dto.InventoryRequestDto;
import com.deliverysystem.productinventory.dto.InventoryResponseDto;
import com.deliverysystem.productinventory.response.ApiResponse;
import com.deliverysystem.productinventory.service.ProductInventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class ProductInventoryController {

    private final ProductInventoryService productInventoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<InventoryResponseDto>> createInventory(
            @Valid @RequestBody InventoryRequestDto requestDto) {
        InventoryResponseDto response = productInventoryService.createInventory(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Inventory created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<InventoryResponseDto>>> getAllInventory() {
        List<InventoryResponseDto> inventoryList = productInventoryService.getAllInventory();
        return ResponseEntity.ok(ApiResponse.success("Fetched all inventory", inventoryList));
    }

    @GetMapping("/{productName}")
    public ResponseEntity<ApiResponse<InventoryResponseDto>> getByProductName(
            @PathVariable String productName) {
        InventoryResponseDto response = productInventoryService.getByProductName(productName);
        return ResponseEntity.ok(ApiResponse.success("Fetched inventory by product name", response));
    }
}
