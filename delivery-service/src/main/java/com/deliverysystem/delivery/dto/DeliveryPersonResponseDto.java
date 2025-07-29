package com.deliverysystem.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryPersonResponseDto {

    private Long id;
    private String name;
    private String phoneNumber;
    private Boolean available;
    private Integer currentLoad;
    private Integer maxLoad;
}