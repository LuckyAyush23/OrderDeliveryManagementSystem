package com.deliverysystem.productinventory.dto;

import com.deliverysystem.productinventory.enums.StockStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryResponseDto {

    private Long productId;
    private String productName;
    private String description;
    private BigDecimal price;
    private int quantity;
    private String warehouseLocation;
    private StockStatus stockStatus;
}
