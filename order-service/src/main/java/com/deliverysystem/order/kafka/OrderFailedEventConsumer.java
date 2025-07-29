package com.deliverysystem.order.kafka;

import com.deliverysystem.order.entity.Order;
import com.deliverysystem.order.enums.OrderStatus;
import com.deliverysystem.order.events.OrderFailedEvent;
import com.deliverysystem.order.exception.ResourceNotFoundException;
import com.deliverysystem.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderFailedEventConsumer {

    private final OrderRepository orderRepository;

    @KafkaListener(
            topics = "${topic.order.failed}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeOrderFailedEvent(OrderFailedEvent event) {
        log.warn("Received OrderFailedEvent: {}", event);

        Order order = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.setStatus(OrderStatus.FAILED);
        orderRepository.save(order);
    }
}
