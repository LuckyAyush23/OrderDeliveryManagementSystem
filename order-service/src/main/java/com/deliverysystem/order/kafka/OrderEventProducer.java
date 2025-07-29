package com.deliverysystem.order.kafka;


import com.deliverysystem.order.events.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic.order}")
    private String orderTopic;

    public void sendOrderCreatedEvent(OrderPlacedEvent event) {
        log.info("Publishing order event to Kafka and order id is : {}", event.getOrderId());
        kafkaTemplate.send(orderTopic, event);
    }
}

