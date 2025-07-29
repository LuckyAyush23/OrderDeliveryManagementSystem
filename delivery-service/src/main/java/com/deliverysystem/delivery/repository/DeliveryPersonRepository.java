package com.deliverysystem.delivery.repository;

import com.deliverysystem.delivery.entity.DeliveryPerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryPersonRepository extends JpaRepository<DeliveryPerson, Long> {
    Optional<DeliveryPerson> findTopByAvailableTrueAndCurrentLoadLessThan(int maxLoad);


}

