package com.deliverysystem.delivery.service;

import com.deliverysystem.delivery.dto.DeliveryPersonRequestDto;
import com.deliverysystem.delivery.dto.DeliveryPersonResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DeliveryPersonService {
    DeliveryPersonResponseDto createDeliveryPerson(DeliveryPersonRequestDto dto);
    DeliveryPersonResponseDto getById(Long id);
    List<DeliveryPersonResponseDto> getAll();
}