package com.deliverysystem.order.kafka;

import com.deliverysystem.order.events.OrderStatusUpdateEvent;
import com.deliverysystem.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderStatusEventListener {

    private final OrderRepository orderRepository;

    @KafkaListener(topics = "order-status-topic", groupId = "order-group", containerFactory = "kafkaDeliveryListenerContainerFactory")
    public void handleOrderStatusUpdate(OrderStatusUpdateEvent event) {
        log.info("Received status update for order: {} with status: {}", event.getOrderId(), event.getStatus());

        orderRepository.findById(event.getOrderId()).ifPresentOrElse(order -> {
            order.setStatus(event.getStatus());
            orderRepository.save(order);
            log.info("Order {} status updated to {}", order.getId(), order.getStatus());
        }, () -> {
            log.warn("Order with ID {} not found!", event.getOrderId());
        });
    }
}