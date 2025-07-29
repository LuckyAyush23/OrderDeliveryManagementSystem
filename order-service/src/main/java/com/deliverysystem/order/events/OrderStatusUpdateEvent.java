package com.deliverysystem.order.events;

import com.deliverysystem.order.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderStatusUpdateEvent {
    private Long orderId;
    private OrderStatus status;
}