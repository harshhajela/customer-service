package com.hajela.customerservice.controller;

import com.hajela.customerservice.domain.CustomerDto;

public class CustomerHelper {

    public static CustomerDto createCustomerDto() {
        return CustomerDto.builder()
                .email("harsh.hajela@gmail.com")
                .firstName("Harsh")
                .lastName("Hajela")
                .userName("harsh.hajela")
                .build();
    }
}
