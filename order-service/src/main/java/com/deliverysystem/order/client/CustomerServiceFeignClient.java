package com.deliverysystem.order.client;

import com.deliverysystem.order.dto.CustomerResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service", url = "${customer.service.url}")
public interface CustomerServiceFeignClient {

    @GetMapping("/api/customers/{id}")
    ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable("id") Long id);
}
