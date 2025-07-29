package com.deliverysystem.customer.dto;

import com.deliverysystem.customer.enums.Gender;
import lombok.Data;


@Data
public class CustomerResponseDto {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private Gender gender;
    private String address;
    private boolean isActive;
}