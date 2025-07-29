package com.deliverysystem.payment.dto;

import com.deliverysystem.payment.enums.PaymentMode;
import com.deliverysystem.payment.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDto {

    private Long id;

    private Long orderId;

    private BigDecimal amount;

    private PaymentMode paymentMode;

    private PaymentStatus status;

    private LocalDateTime createdAt;
}