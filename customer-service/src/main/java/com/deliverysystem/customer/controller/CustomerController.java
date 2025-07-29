package com.deliverysystem.customer.controller;

import com.deliverysystem.customer.dto.CustomerRequestDto;
import com.deliverysystem.customer.dto.CustomerResponseDto;
import com.deliverysystem.customer.response.ApiResponse;
import com.deliverysystem.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Slf4j
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponseDto>> registerCustomer(@Valid @RequestBody CustomerRequestDto dto) {
        log.info("Creating new customer with email: {}", dto.getEmail());
        CustomerResponseDto saved = customerService.createCustomer(dto);
        return new ResponseEntity<>(ApiResponse.success("Customer registered successfully", saved), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponseDto>> getCustomer(@PathVariable Long id) {
        log.info("Fetching customer with ID: {}", id);
        return ResponseEntity.ok(ApiResponse.success("Customer fetched successfully", customerService.getCustomerById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerResponseDto>>> getAllCustomers() {
        log.info("Fetching all customers");
        return ResponseEntity.ok(ApiResponse.success("All customers fetched", customerService.getAllCustomers()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponseDto>> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerRequestDto dto) {
        log.info("Updating customer with ID: {}", id);
        return ResponseEntity.ok(ApiResponse.success("Customer updated successfully", customerService.updateCustomer(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCustomer(@PathVariable Long id) {
        log.info("Deleting customer with ID: {}", id);
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(ApiResponse.success("Customer deleted successfully", "Deleted ID: " + id));
    }
}