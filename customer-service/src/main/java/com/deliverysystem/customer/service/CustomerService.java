package com.deliverysystem.customer.service;

import com.deliverysystem.customer.dto.CustomerRequestDto;
import com.deliverysystem.customer.dto.CustomerResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService {
    CustomerResponseDto createCustomer(CustomerRequestDto dto);
    CustomerResponseDto getCustomerById(Long id);
    List<CustomerResponseDto> getAllCustomers();
    CustomerResponseDto updateCustomer(Long id, CustomerRequestDto dto);
    void deleteCustomer(Long id);
}