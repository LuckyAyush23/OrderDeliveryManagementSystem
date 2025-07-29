package com.deliverysystem.payment.controller;

import com.deliverysystem.payment.dto.PaymentRequestDto;
import com.deliverysystem.payment.dto.PaymentResponseDto;
import com.deliverysystem.payment.entity.Payment;
import com.deliverysystem.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/{orderId}")
    public ResponseEntity<Payment> getPayment(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.getByOrderId(orderId));
    }
}

