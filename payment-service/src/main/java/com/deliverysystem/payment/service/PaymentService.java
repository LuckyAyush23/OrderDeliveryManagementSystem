package com.deliverysystem.payment.service;

import com.deliverysystem.payment.entity.Payment;
import com.deliverysystem.payment.events.OrderPlacedEvent;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {
    void processPayment(OrderPlacedEvent event);

    Payment getByOrderId(Long orderId);

}
