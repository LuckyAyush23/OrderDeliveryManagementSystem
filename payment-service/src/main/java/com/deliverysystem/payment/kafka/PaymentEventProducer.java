package com.deliverysystem.payment.kafka;

import com.deliverysystem.payment.events.PaymentCompletedEvent;
import com.deliverysystem.payment.events.PaymentFailedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendPaymentCompletedEvent(PaymentCompletedEvent event) {
        log.info("Sending PaymentCompletedEvent for Order ID: {}, Amount: {}", event.getOrderId(), event.getAmount());
        kafkaTemplate.send("payment-success-topic", event);
    }

    public void sendPaymentFailedEvent(PaymentFailedEvent event) {
        log.info("Sending PaymentFailedEvent for Order ID: {}", event.getOrderId());
        kafkaTemplate.send("payment-failed-topic", event);
    }
}


