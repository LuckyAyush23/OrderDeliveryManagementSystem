package com.deliverysystem.delivery.kafka;

import com.deliverysystem.delivery.events.PaymentCompletedEvent;
import com.deliverysystem.delivery.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventsListener {
    private final DeliveryService deliveryService;

    @KafkaListener(topics="payment-success-topic", containerFactory="paymentCompletedKafkaListenerContainerFactory")
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        deliveryService.processDeliveryCreation(event);
    }
}
