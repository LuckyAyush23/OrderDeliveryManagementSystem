package com.deliverysystem.payment.kafka;

import com.deliverysystem.payment.entity.Payment;
import com.deliverysystem.payment.enums.PaymentStatus;
import com.deliverysystem.payment.events.OrderPlacedEvent;
import com.deliverysystem.payment.events.PaymentCompletedEvent;
import com.deliverysystem.payment.repository.PaymentRepository;
import com.deliverysystem.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventConsumer {

    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;

    @KafkaListener(
            topics = "${kafka.topic.payment}",
            groupId = "payment-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumePaymentInitiatedEvent(@Valid OrderPlacedEvent event) {
        if (event.getOrderId() == null) {
            log.error("Invalid OrderPlacedEvent received: {}", event);
            return;
        }
        log.info("Received PaymentInitiatedEvent: {}", event);
        paymentService.processPayment(event);
    }

    @KafkaListener(
            topics = "${cod-payment-success-topic}",
            groupId = "payment-group",
            containerFactory = "kafkaPaymentListenerContainerFactory"
    )
    public void consumeCodPaymentSuccess(PaymentCompletedEvent event) {
        log.info("Received COD Payment Success Event: {}", event);

        Payment payment = paymentRepository.findById(event.getPaymentId())
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaymentMode(event.getPaymentMode());

        paymentRepository.save(payment);

        log.info("Saved COD payment for orderId: {} with paymentId: {}", event.getOrderId(), event.getPaymentId());
    }

}

