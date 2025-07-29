package com.deliverysystem.customer.service.impl;

import com.deliverysystem.customer.dto.CustomerRequestDto;
import com.deliverysystem.customer.dto.CustomerResponseDto;
import com.deliverysystem.customer.entity.Customer;
import com.deliverysystem.customer.exception.DuplicateResourceException;
import com.deliverysystem.customer.exception.ResourceNotFoundException;
import com.deliverysystem.customer.repository.CustomerRepository;
import com.deliverysystem.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    @Override
    public CustomerResponseDto createCustomer(CustomerRequestDto requestDto) {
        log.info("Creating customer with email: {}", requestDto.getEmail());

        if (customerRepository.existsByEmail(requestDto.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + requestDto.getEmail());
        }

        if (customerRepository.existsByPhone(requestDto.getPhone())) {
            throw new DuplicateResourceException("Phone already exists: " + requestDto.getPhone());
        }
        Customer customer = Customer.builder()
                .fullName(requestDto.getFullName())
                .email(requestDto.getEmail())
                .password(requestDto.getPassword())
                .phone(requestDto.getPhone())
                .gender(requestDto.getGender())
                .address(requestDto.getAddress())
                .isActive(true)
                .build();

        Customer savedCustomer = customerRepository.save(customer);
        return modelMapper.map(savedCustomer, CustomerResponseDto.class);
    }

    @Override
    public CustomerResponseDto getCustomerById(Long id) {
        log.info("Fetching customer by ID: {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));

        return modelMapper.map(customer, CustomerResponseDto.class);
    }

    @Override
    public List<CustomerResponseDto> getAllCustomers() {
        log.info("Fetching all customers");

        List<Customer> customers = customerRepository.findAll();

        return customers.stream()
                .map(customer -> modelMapper.map(customer, CustomerResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponseDto updateCustomer(Long id, CustomerRequestDto requestDto) {
        log.info("Updating customer with ID: {}", id);

        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));

        Customer updatedCustomer = existingCustomer.toBuilder()
                .fullName(requestDto.getFullName())
                .phone(requestDto.getPhone())
                .gender(requestDto.getGender())
                .address(requestDto.getAddress())
                .build();

        Customer savedCustomer = customerRepository.save(updatedCustomer);
        return modelMapper.map(savedCustomer, CustomerResponseDto.class);
    }

    @Override
    public void deleteCustomer(Long id) {
        log.info("Deleting customer with ID: {}", id);

        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));

        customerRepository.delete(existingCustomer);
    }
}