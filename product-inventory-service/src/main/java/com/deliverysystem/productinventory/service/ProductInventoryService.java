package com.deliverysystem.productinventory.service;

import com.deliverysystem.productinventory.dto.InventoryRequestDto;
import com.deliverysystem.productinventory.dto.InventoryResponseDto;
import com.deliverysystem.productinventory.events.OrderPlacedEvent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductInventoryService {
    void handleOrderPlaced(OrderPlacedEvent event);
    InventoryResponseDto createInventory(InventoryRequestDto requestDto);
    List<InventoryResponseDto> getAllInventory();
    InventoryResponseDto getByProductName(String productName);

}
