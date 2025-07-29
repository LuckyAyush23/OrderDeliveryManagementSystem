package com.deliverysystem.order.events;

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

}

