package com.deliverysystem.productinventory.service.impl;

import com.deliverysystem.productinventory.dto.InventoryRequestDto;
import com.deliverysystem.productinventory.dto.InventoryResponseDto;
import com.deliverysystem.productinventory.entity.ProductInventory;
import com.deliverysystem.productinventory.enums.StockStatus;
import com.deliverysystem.productinventory.events.OrderPlacedEvent;
import com.deliverysystem.productinventory.exception.DuplicateResourceException;
import com.deliverysystem.productinventory.kafka.InventoryEventProducer;
import com.deliverysystem.productinventory.repository.ProductInventoryRepository;
import com.deliverysystem.productinventory.service.ProductInventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductInventoryServiceImpl implements ProductInventoryService {

    private final ProductInventoryRepository productInventoryRepository;
    private final InventoryEventProducer eventProducer;
    private final ModelMapper modelMapper;

    @Override
    public void handleOrderPlaced(OrderPlacedEvent event) {
        Optional<ProductInventory> inventoryOpt = productInventoryRepository.findByProductId(event.getProductId());

        if (inventoryOpt.isEmpty()) {
            log.error("No inventory for product: {}", event.getProductId());
            eventProducer.sendOrderFailedEvent(event.getOrderId(), "Product not found in inventory");
            return;
        }

        ProductInventory inventory = inventoryOpt.get();

        if (inventory.getQuantity() < event.getQuantity()) {
            log.warn("Not enough stock for product: {}", event.getProductId());
            eventProducer.sendOrderFailedEvent(event.getOrderId(), "Insufficient inventory");
            return;
        }

        BigDecimal unitPrice = inventory.getPrice();
        BigDecimal quantity = BigDecimal.valueOf(event.getQuantity());
        BigDecimal totalAmount = unitPrice.multiply(quantity);

        inventory.setQuantity(inventory.getQuantity() - event.getQuantity());


        event.setAmount(totalAmount);
        productInventoryRepository.save(inventory);

        log.info("Inventory updated. Quantity left: {}. Total price: {}", inventory.getQuantity(), totalAmount);

        eventProducer.sendPaymentInitiateEvent(event);
    }

    @Override
    public InventoryResponseDto createInventory(InventoryRequestDto requestDto) {
        ProductInventory inventory = modelMapper.map(requestDto, ProductInventory.class);

        Optional<ProductInventory> existing = productInventoryRepository.findByProductName(requestDto.getProductName());
        if (existing.isPresent()) {
            throw new DuplicateResourceException("Product with this name already exists");
        }
        // Set stock status
        inventory.setStockStatus(inventory.getQuantity() > 0 ? StockStatus.IN_STOCK : StockStatus.OUT_OF_STOCK);

        ProductInventory saved = productInventoryRepository.save(inventory);
        return modelMapper.map(saved, InventoryResponseDto.class);
    }

    @Override
    public List<InventoryResponseDto> getAllInventory() {
        return productInventoryRepository.findAll()
                .stream()
                .map(inventory -> modelMapper.map(inventory, InventoryResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public InventoryResponseDto getByProductName(String productName) {
        ProductInventory inventory = productInventoryRepository.findByProductName(productName)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productName));
        return modelMapper.map(inventory, InventoryResponseDto.class);
    }
}



