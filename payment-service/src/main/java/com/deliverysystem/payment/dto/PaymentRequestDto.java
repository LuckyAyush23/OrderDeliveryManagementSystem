package com.deliverysystem.payment.dto;

import com.deliverysystem.payment.enums.PaymentMode;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestDto {

    @NotNull(message = "Order ID must not be null")
    private Long orderId;

    @NotNull(message = "Amount must not be null")
    @DecimalMin(value = "0.1", inclusive = true, message = "Amount must be at least 0.1")
    private BigDecimal amount;

    @NotNull(message = "Payment mode must not be null")
    private PaymentMode paymentMode;
}