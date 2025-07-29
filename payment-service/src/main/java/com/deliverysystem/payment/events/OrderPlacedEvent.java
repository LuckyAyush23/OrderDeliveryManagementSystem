package com.deliverysystem.payment.events;

import com.deliverysystem.payment.enums.OrderStatus;
import com.deliverysystem.payment.enums.PaymentMode;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderPlacedEvent {
    private Long orderId;
    private Long productId;
    private int quantity;

    private PaymentMode paymentMode;
    private OrderStatus status;
    private LocalDateTime createdAt;

    private BigDecimal amount;
}