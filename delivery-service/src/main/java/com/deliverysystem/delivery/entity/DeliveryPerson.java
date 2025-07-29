package com.deliverysystem.delivery.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "delivery_persons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryPerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid phone number")
    @Column(unique = true)
    private String phoneNumber;

    @NotNull
    @Builder.Default
    private Boolean available = true;

    @Builder.Default
    private Integer currentLoad = 0;

    @NotNull
    private Integer maxLoad;
}