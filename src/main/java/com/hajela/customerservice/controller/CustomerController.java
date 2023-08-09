package com.hajela.customerservice.controller;

import com.hajela.customerservice.domain.AllCustomers;
import com.hajela.customerservice.domain.CreateCustomer;
import com.hajela.customerservice.domain.CustomerDto;
import com.hajela.customerservice.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AllCustomers> getAllCustomers(@PageableDefault(page = 0, size = 5) Pageable pageable) {
        return new ResponseEntity(customerService.getAllCustomers(pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable(value = "customerId") Integer customerId) {
        return new ResponseEntity(customerService.getCustomer(customerId), HttpStatus.OK);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity saveCustomer(@Valid @RequestBody CreateCustomer createCustomer) {
        var createdCustomer = customerService.create(createCustomer);
        if (createdCustomer.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{customerId}")
                    .build(createdCustomer.get().getCustomerId());
            return ResponseEntity.created(uri)
                    .body(CustomerDto.from(createdCustomer.get()));
        }
    }

    @PutMapping(value="/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateCustomer(@PathVariable(value = "customerId") Integer customerId,
                                         @Valid @RequestBody CreateCustomer updateCustomer) {
        var updatedCustomer = customerService.updateCustomer(customerId, updateCustomer);
        if (updatedCustomer.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{customerId}")
                    .build(updatedCustomer.get().getCustomerId());
            return ResponseEntity.created(uri)
                    .body(CustomerDto.from(updatedCustomer.get()));
        }
    }
}
