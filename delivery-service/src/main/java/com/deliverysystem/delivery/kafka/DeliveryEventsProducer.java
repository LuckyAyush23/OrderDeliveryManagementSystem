package com.deliverysystem.delivery.kafka;

import com.deliverysystem.delivery.events.OrderStatusUpdateEvent;
import com.deliverysystem.delivery.events.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryEventsProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;


    public void sendOutForDeliveryEvent(OrderStatusUpdateEvent event) {
        kafkaTemplate.send("order-status-topic", event);
    }

    public void sendDeliveryCompletedEvent(OrderStatusUpdateEvent event) {
        kafkaTemplate.send("order-status-topic", event);
    }

    public void sendCodPaymentCompletedEvent(PaymentCompletedEvent event) {
        kafkaTemplate.send("cod-payment-success-topic", event);  // for payment-service to consume
    }
}

