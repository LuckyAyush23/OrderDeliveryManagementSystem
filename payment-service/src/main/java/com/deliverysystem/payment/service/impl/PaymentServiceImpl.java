package com.deliverysystem.payment.service.impl;

import com.deliverysystem.payment.entity.Payment;
import com.deliverysystem.payment.enums.PaymentMode;
import com.deliverysystem.payment.enums.PaymentStatus;
import com.deliverysystem.payment.events.OrderPlacedEvent;
import com.deliverysystem.payment.events.PaymentCompletedEvent;
import com.deliverysystem.payment.events.PaymentFailedEvent;
import com.deliverysystem.payment.exception.ResourceNotFoundException;
import com.deliverysystem.payment.kafka.PaymentEventProducer;
import com.deliverysystem.payment.repository.PaymentRepository;
import com.deliverysystem.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentEventProducer paymentEventProducer;

    @Override
    public void processPayment(OrderPlacedEvent event) {
        try {
            Payment payment;
            Long orderId = event.getOrderId();
            BigDecimal amount = event.getAmount();
            PaymentMode paymentMode = event.getPaymentMode();

            if (paymentMode == PaymentMode.UPI) {
                boolean isUpiSuccess = Math.random() > 0.2;
                PaymentStatus status = isUpiSuccess ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;

                payment = Payment.builder()
                        .orderId(orderId)
                        .amount(amount)
                        .paymentMode(paymentMode)
                        .status(status)
                        .createdAt(LocalDateTime.now())
                        .build();

                paymentRepository.save(payment);

                if (isUpiSuccess) {
                    PaymentCompletedEvent successEvent = PaymentCompletedEvent.builder()
                            .paymentId(payment.getId())
                            .orderId(orderId)
                            .amount(amount)
                            .timestamp(LocalDateTime.now())
                            .status(PaymentStatus.SUCCESS)
                            .paymentMode(PaymentMode.UPI)
                            .build();
                    log.info("UPI Payment successful for Order ID {}", orderId);
                    paymentEventProducer.sendPaymentCompletedEvent(successEvent);

                } else {
                    PaymentFailedEvent failedEvent = PaymentFailedEvent.builder()
                            .paymentId(payment.getId())
                            .orderId(orderId)
                            .reason("UPI payment failed")
                            .timestamp(LocalDateTime.now())
                            .status(PaymentStatus.FAILED)
                            .build();
                    log.warn("UPI Payment failed for Order ID {}", orderId);
                    paymentEventProducer.sendPaymentFailedEvent(failedEvent);
                }

            } else if (paymentMode == PaymentMode.COD) {
                payment = Payment.builder()
                        .orderId(orderId)
                        .amount(amount)
                        .paymentMode(PaymentMode.COD)
                        .status(PaymentStatus.PENDING)
                        .createdAt(LocalDateTime.now())
                        .build();

                paymentRepository.save(payment);

                PaymentCompletedEvent codEvent = PaymentCompletedEvent.builder()
                        .paymentId(payment.getId())
                        .orderId(orderId)
                        .amount(amount)
                        .timestamp(LocalDateTime.now())
                        .status(PaymentStatus.PENDING)
                        .paymentMode(PaymentMode.COD)
                        .build();

                paymentEventProducer.sendPaymentCompletedEvent(codEvent);
                log.info("COD Payment marked as PENDING for Order ID {}", orderId);

            } else {
                log.error("Unsupported payment mode: {}", paymentMode);
            }

        } catch (Exception e) {
            log.error("Error processing payment for Order ID {}: {}", event.getOrderId(), e.getMessage());
        }
    }

    @Override
    public Payment getByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for orderId: " + orderId));
    }
}