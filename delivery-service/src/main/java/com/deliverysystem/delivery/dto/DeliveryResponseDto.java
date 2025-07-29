package com.deliverysystem.delivery.dto;

import com.deliverysystem.delivery.enums.DeliveryStatus;
import com.deliverysystem.delivery.enums.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryResponseDto {

    private Long id;
    private Long orderId;
    private DeliveryStatus status;
    private PaymentMode paymentMode;
    private DeliveryPersonDtoForDelivery deliveryPerson;
}