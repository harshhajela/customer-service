package com.hajela.customerservice.service;

import com.hajela.customerservice.domain.AllCustomers;
import com.hajela.customerservice.domain.CreateCustomer;
import com.hajela.customerservice.domain.CustomerDto;
import com.hajela.customerservice.entities.CustomerEntity;
import com.hajela.customerservice.exceptions.CustomerNotFound;
import com.hajela.customerservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@SuppressWarnings("ClassCanBeRecord")
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public AllCustomers getAllCustomers(Pageable pageable) {
        Page<CustomerEntity> entityList = customerRepository.findAll(pageable);
        return AllCustomers.builder()
                .customers(entityList.stream().map(CustomerDto::from).toList())
                .totalItems(entityList.getNumberOfElements())
                .totalPages(entityList.getTotalPages())
                .build();
    }

    public CustomerDto getCustomer(Integer customerId) {
        var customerOptional = customerRepository.findByCustomerId(customerId);
        if (customerOptional.isPresent()) {
            return CustomerDto.from(customerOptional.get());
        }
        throw new CustomerNotFound("Customer not found with ID: " + customerId);
    }

    public Optional<CustomerEntity> create(CreateCustomer customerDto) {
        var entity = CustomerEntity.from(customerDto);
        return Optional.of(customerRepository.save(entity));
    }

    public Optional<CustomerEntity> updateCustomer(Integer customerId, CreateCustomer updateCustomer) {
        var customerUpdateEntity = CustomerEntity.from(updateCustomer);
        var existingEntityOptional = customerRepository.findByCustomerId(customerId);
        existingEntityOptional.ifPresent(customerEntity -> customerUpdateEntity.setCustomerId(customerEntity.getCustomerId()));
        return Optional.of(customerRepository.save(customerUpdateEntity));
    }
}
