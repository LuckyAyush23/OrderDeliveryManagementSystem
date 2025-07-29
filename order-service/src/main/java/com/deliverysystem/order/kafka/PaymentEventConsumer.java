package com.deliverysystem.order.kafka;

import com.deliverysystem.order.enums.OrderStatus;
import com.deliverysystem.order.events.PaymentCompletedEvent;
import com.deliverysystem.order.events.PaymentFailedEvent;
import com.deliverysystem.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventConsumer {

    private final OrderRepository orderRepository;

    @KafkaListener(
            topics = "${kafka.topic.payment.success}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaPaymentSuccessListenerContainerFactory"
    )
    public void handlePaymentSuccess(PaymentCompletedEvent event) {
        log.info("Received PaymentCompletedEvent: {}", event);

        orderRepository.findById(event.getOrderId()).ifPresent(order -> {
            order.setStatus(OrderStatus.PLACED);
            order.setTotalPrice(event.getAmount());
            orderRepository.save(order);
            log.info("Order status updated to PLACED for orderId {}", event.getOrderId());
        });
    }


    @KafkaListener(
            topics = "${kafka.topic.payment.failed}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaPaymentFailedListenerContainerFactory"
    )
    public void handlePaymentFailed(PaymentFailedEvent event) {
        log.warn("Received PaymentFailedEvent: {}", event);

        orderRepository.findById(event.getOrderId()).ifPresent(order -> {
            order.setStatus(OrderStatus.FAILED);
            orderRepository.save(order);
            log.warn("Order status updated to FAILED for orderId {}", event.getOrderId());
        });
    }

}