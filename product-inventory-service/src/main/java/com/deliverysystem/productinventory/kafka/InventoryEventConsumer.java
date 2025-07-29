package com.deliverysystem.productinventory.kafka;


import com.deliverysystem.productinventory.events.OrderPlacedEvent;
import com.deliverysystem.productinventory.service.ProductInventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryEventConsumer {

    private final ProductInventoryService inventoryService;

    @KafkaListener(
            topics = "${kafka.topic.order}",
            groupId = "inventory-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeOrderPlacedEvent(@Valid OrderPlacedEvent event) {
        if (event.getOrderId()==null) {
            log.error("Invalid OrderPlacedEvent received: {}", event);
            return;
        }
        log.info("OrderPlacedEvent Received for Order ID: {}", event.getOrderId());

        inventoryService.handleOrderPlaced(event);
    }
}
