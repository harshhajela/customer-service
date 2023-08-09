package com.hajela.customerservice.domain;

import com.hajela.customerservice.entities.CustomerEntity;
import jakarta.persistence.Transient;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDto {
    @Transient
    private Integer id;
    private String email;
    private String userName;
    private String firstName;
    private String lastName;

    public static CustomerDto from(CustomerEntity customer) {
        return CustomerDto.builder()
                .id(customer.getCustomerId())
                .email(customer.getEmail())
                .userName(customer.getUserName())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .build();
    }
}
