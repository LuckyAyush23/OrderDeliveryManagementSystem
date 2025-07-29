package com.deliverysystem.customer.dto;

import com.deliverysystem.customer.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CustomerRequestDto {

    @NotBlank
    @Size(min = 2, max = 50)
    private String fullName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 30, message = "Password must be between 6 to 30 characters")
    private String password;

    @NotBlank
    @Pattern(regexp = "\\d{10}", message = "Phone must be 10 digits")
    private String phone;

    private Gender gender;

    @NotBlank
    @Size(max = 255)
    private String address;
}