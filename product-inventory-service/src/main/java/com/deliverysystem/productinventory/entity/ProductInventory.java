package com.deliverysystem.productinventory.entity;


import com.deliverysystem.productinventory.enums.StockStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "product_inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;


    @Column(unique = true, nullable = false)
    private String productName;

    private String description;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Min(value = 0, message = "Quantity cannot be negative")
    private int quantity;

    private String warehouseLocation;

    @Enumerated(EnumType.STRING)
    private StockStatus stockStatus; // Enum: IN_STOCK, OUT_OF_STOCK
}

