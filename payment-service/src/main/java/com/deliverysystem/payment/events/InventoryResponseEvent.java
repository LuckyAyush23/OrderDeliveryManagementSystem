package com.deliverysystem.payment.events;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryResponseEvent {
    private Long orderId;
    private Long productId;
    private int quantity;
    private boolean available;
    private BigDecimal amount;
}
