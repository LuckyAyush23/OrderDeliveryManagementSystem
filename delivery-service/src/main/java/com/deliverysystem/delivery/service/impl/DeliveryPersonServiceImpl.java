package com.deliverysystem.delivery.service.impl;

import com.deliverysystem.delivery.dto.DeliveryPersonRequestDto;
import com.deliverysystem.delivery.dto.DeliveryPersonResponseDto;
import com.deliverysystem.delivery.entity.DeliveryPerson;
import com.deliverysystem.delivery.repository.DeliveryPersonRepository;
import com.deliverysystem.delivery.response.ApiResponse;
import com.deliverysystem.delivery.service.DeliveryPersonService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryPersonServiceImpl implements DeliveryPersonService {

    private final DeliveryPersonRepository deliveryPersonRepository;
    private final ModelMapper modelMapper;

    @Override
    public DeliveryPersonResponseDto createDeliveryPerson(DeliveryPersonRequestDto dto) {
        DeliveryPerson person = modelMapper.map(dto, DeliveryPerson.class);
        DeliveryPerson saved = deliveryPersonRepository.save(person);
        return modelMapper.map(saved, DeliveryPersonResponseDto.class);
    }

    @Override
    public DeliveryPersonResponseDto getById(Long id) {
        DeliveryPerson person = deliveryPersonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery person not found with ID: " + id));
        return modelMapper.map(person, DeliveryPersonResponseDto.class);
    }

    @Override
    public List<DeliveryPersonResponseDto> getAll() {
        List<DeliveryPerson> list = deliveryPersonRepository.findAll();
        return list.stream()
                .map(person -> modelMapper.map(person, DeliveryPersonResponseDto.class))
                .collect(Collectors.toList());
    }


}