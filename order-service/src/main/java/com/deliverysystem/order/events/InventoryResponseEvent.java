package com.deliverysystem.order.events;

import com.deliverysystem.order.enums.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponseEvent {

    private Long orderId;
    private String productName;
    private int quantity;
    private BigDecimal productPrice;
    private boolean success;
    private String message;
    private PaymentMode paymentMode;
}