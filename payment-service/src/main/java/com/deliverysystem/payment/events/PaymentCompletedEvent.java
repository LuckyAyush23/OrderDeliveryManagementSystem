package com.deliverysystem.payment.events;

import com.deliverysystem.payment.enums.PaymentMode;
import com.deliverysystem.payment.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentCompletedEvent {
    private Long paymentId;
    private Long orderId;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private PaymentStatus status;
    private PaymentMode paymentMode;
}