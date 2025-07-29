package com.deliverysystem.delivery.repository;

import com.deliverysystem.delivery.entity.Delivery;
import com.deliverysystem.delivery.enums.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Optional<Delivery> findByOrderId(Long orderId);
    int countByDeliveryPersonIdAndStatusIn(Long deliveryPersonId, List<DeliveryStatus> statuses);

}
