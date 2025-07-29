package com.deliverysystem.order.kafka;

import com.deliverysystem.order.entity.Order;
import com.deliverysystem.order.enums.OrderStatus;
import com.deliverysystem.order.events.InventoryResponseEvent;
import com.deliverysystem.order.exception.ResourceNotFoundException;
import com.deliverysystem.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryEventConsumer {

    private final OrderRepository orderRepository;

    @KafkaListener(
            topics = "${topic.inventory.response}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeInventoryResponse(Object object) {
        if (!(object instanceof InventoryResponseEvent)) {
            log.error("Invalid message type for InventoryResponseEvent: {}", object);
            return;
        }

        InventoryResponseEvent event = (InventoryResponseEvent) object;
        log.info("📦 Received InventoryResponseEvent: {}", event);

        Order order = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (event.isSuccess()) {
            BigDecimal totalPrice = event.getProductPrice().multiply(BigDecimal.valueOf(event.getQuantity()));
            order.setTotalPrice(totalPrice);
        } else {
            order.setStatus(OrderStatus.FAILED);
        }

        orderRepository.save(order);
    }
}
