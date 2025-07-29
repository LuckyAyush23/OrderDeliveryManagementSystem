package com.deliverysystem.delivery.service.impl;

import com.deliverysystem.delivery.dto.DeliveryResponseDto;
import com.deliverysystem.delivery.entity.Delivery;
import com.deliverysystem.delivery.entity.DeliveryPerson;
import com.deliverysystem.delivery.enums.DeliveryStatus;
import com.deliverysystem.delivery.enums.PaymentMode;
import com.deliverysystem.delivery.enums.PaymentStatus;
import com.deliverysystem.delivery.events.OrderStatusUpdateEvent;
import com.deliverysystem.delivery.events.PaymentCompletedEvent;
import com.deliverysystem.delivery.exception.NoDeliveryPersonAvailableException;
import com.deliverysystem.delivery.exception.ResourceNotFoundException;
import com.deliverysystem.delivery.kafka.DeliveryEventsProducer;
import com.deliverysystem.delivery.repository.DeliveryPersonRepository;
import com.deliverysystem.delivery.repository.DeliveryRepository;
import com.deliverysystem.delivery.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryPersonRepository deliveryPersonRepository;
    private final DeliveryEventsProducer producer;
    private final ModelMapper modelMapper;


    @Override
    public DeliveryResponseDto getDeliveryById(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found with ID: " + id));
        return modelMapper.map(delivery, DeliveryResponseDto.class);
    }

    @Override
    public List<DeliveryResponseDto> getAllDeliveries() {
        return deliveryRepository.findAll().stream()
                .map(delivery -> modelMapper.map(delivery, DeliveryResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void processDeliveryCreation(PaymentCompletedEvent event) {
        log.info("Starting delivery creation process for Order ID: {}", event.getOrderId());

        DeliveryPerson availablePerson = deliveryPersonRepository
                .findTopByAvailableTrueAndCurrentLoadLessThan(5)
                .orElseThrow(() -> {
                    log.warn("No delivery person available at the moment for Order ID: {}", event.getOrderId());
                    return new NoDeliveryPersonAvailableException("No delivery person available. Scheduler will retry.");
                });

        log.info("Assigned DeliveryPerson ID: {} for Order ID: {}", availablePerson.getId(), event.getOrderId());

        Delivery delivery = Delivery.builder()
                .orderId(event.getOrderId())
                .paymentId(event.getPaymentId())
                .paymentMode(event.getPaymentMode())
                .paymentStatus(event.getStatus())
                .status(DeliveryStatus.OUT_FOR_DELIVERY)
                .deliveryPerson(availablePerson)
                .build();

        availablePerson.setCurrentLoad(availablePerson.getCurrentLoad() + 1);
        if (availablePerson.getCurrentLoad() >= availablePerson.getMaxLoad()) {
            availablePerson.setAvailable(false);
        }

        deliveryPersonRepository.save(availablePerson);
        log.info("Updated DeliveryPerson (ID: {}) load to {}", availablePerson.getId(), availablePerson.getCurrentLoad());

        deliveryRepository.save(delivery);
        log.info("Delivery record created for Order ID: {}", event.getOrderId());

        producer.sendOutForDeliveryEvent(
                OrderStatusUpdateEvent.builder()
                        .orderId(event.getOrderId())
                        .status("OUT_FOR_DELIVERY")
                        .build()
        );
        log.info("OUT_FOR_DELIVERY event sent for Order ID: {}", event.getOrderId());
    }

    @Override
    public String markAsDelivered(Long orderId) {
        log.info("Initiating mark-as-delivered for Order ID: {}", orderId);

        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> {
                    log.error("Delivery not found for Order ID: {}", orderId);
                    return new RuntimeException("Delivery not found for orderId: " + orderId);
                });

        log.info("Delivery record found for Order ID: {}", orderId);

        if (delivery.getPaymentStatus() == PaymentStatus.PENDING &&
                delivery.getPaymentMode() == PaymentMode.COD) {

            log.info("COD detected with pending payment. Sending PaymentCompletedEvent for Order ID: {}", orderId);

            producer.sendCodPaymentCompletedEvent(
                    PaymentCompletedEvent.builder()
                            .orderId(delivery.getOrderId())
                            .paymentId(delivery.getPaymentId())
                            .paymentMode(delivery.getPaymentMode())
                            .status(PaymentStatus.SUCCESS)
                            .timestamp(LocalDateTime.now())
                            .build()
            );

            delivery.setPaymentStatus(PaymentStatus.SUCCESS);
            log.info("Payment status updated to SUCCESS for COD Order ID: {}", orderId);
        }

        if (delivery.getPaymentStatus() != PaymentStatus.SUCCESS) {
            log.error("Cannot mark as delivered. Payment still not completed for Order ID: {}", orderId);
            throw new RuntimeException("Payment not completed yet. Cannot mark delivered.");
        }

        delivery.setStatus(DeliveryStatus.DELIVERED);
        deliveryRepository.save(delivery);
        log.info("Delivery marked as DELIVERED for Order ID: {}", orderId);

        // Reduce DeliveryPerson load
        DeliveryPerson person = delivery.getDeliveryPerson();
        if (person != null) {
            person.setCurrentLoad(person.getCurrentLoad() - 1);
            if (person.getCurrentLoad() < person.getMaxLoad()) {
                person.setAvailable(true);
            }
            deliveryPersonRepository.save(person);
            log.info("DeliveryPerson ID: {} load reduced to {}", person.getId(), person.getCurrentLoad());
        }

        producer.sendDeliveryCompletedEvent(
                OrderStatusUpdateEvent.builder()
                        .orderId(orderId)
                        .status("DELIVERED")
                        .build()
        );
        log.info("DELIVERED event sent for Order ID: {}", orderId);

        return "Delivery marked as completed.";
    }


}