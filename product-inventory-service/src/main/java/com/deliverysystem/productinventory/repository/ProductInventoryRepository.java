package com.deliverysystem.productinventory.repository;

import com.deliverysystem.productinventory.entity.ProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductInventoryRepository extends JpaRepository<ProductInventory, Long> {
    Optional<ProductInventory> findByProductId(Long id);
    Optional<ProductInventory> findByProductName(String productName);

}
