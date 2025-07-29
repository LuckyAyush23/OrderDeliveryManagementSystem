package com.deliverysystem.payment.events;

import com.deliverysystem.payment.enums.PaymentMode;
import com.deliverysystem.payment.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentFailedEvent {
    private Long paymentId;
    private Long orderId;
    private String reason;
    private LocalDateTime timestamp;
    private PaymentStatus status = PaymentStatus.FAILED;
    private PaymentMode paymentMode;
}

