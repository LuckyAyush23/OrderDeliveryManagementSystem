package com.deliverysystem.order.dto;


import com.deliverysystem.order.enums.OrderStatus;
import com.deliverysystem.order.enums.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDto {

    private Long orderId;

    private Long customerId;

    private Long productId;

    private Integer quantity;

    private BigDecimal totalPrice;

    private LocalDateTime createdAt;

    private OrderStatus status;

    private PaymentMode paymentMode;

}
