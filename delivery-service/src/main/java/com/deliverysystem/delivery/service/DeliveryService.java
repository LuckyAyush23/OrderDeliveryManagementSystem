package com.deliverysystem.delivery.service;

import com.deliverysystem.delivery.dto.*;
import com.deliverysystem.delivery.events.PaymentCompletedEvent;
import com.deliverysystem.delivery.response.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DeliveryService {
    DeliveryResponseDto getDeliveryById(Long id);
    List<DeliveryResponseDto> getAllDeliveries();
    String markAsDelivered(Long orderId);
    void processDeliveryCreation(PaymentCompletedEvent event);

}