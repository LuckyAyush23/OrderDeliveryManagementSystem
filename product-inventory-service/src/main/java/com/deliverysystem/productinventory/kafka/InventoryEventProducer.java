package com.deliverysystem.productinventory.kafka;

import com.deliverysystem.productinventory.events.OrderFailedEvent;
import com.deliverysystem.productinventory.events.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic.payment}")
    private String paymentTopic;

    @Value("${kafka.topic.order-failed}")
    private String orderFailedTopic;

    public void sendPaymentInitiateEvent(OrderPlacedEvent event) {
        log.info("Sending PaymentInitiateEvent for Order {}", event.getOrderId());
        kafkaTemplate.send(paymentTopic, event);
    }

    public void sendOrderFailedEvent(Long orderId, String reason) {
        log.info("Sending OrderFailedEvent for Order {}, Reason: {}", orderId, reason);
        OrderFailedEvent event = new OrderFailedEvent(orderId, reason);
        kafkaTemplate.send(orderFailedTopic, event);
    }
}
