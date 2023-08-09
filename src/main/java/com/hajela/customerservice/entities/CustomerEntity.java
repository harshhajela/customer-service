package com.hajela.customerservice.entities;

import com.hajela.customerservice.domain.CreateCustomer;
import com.hajela.customerservice.domain.CustomerDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "customer")
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer customerId;

    private String email;
    private String userName;
    private String firstName;
    private String lastName;
    private Timestamp created;

    public static CustomerEntity from(CreateCustomer customerDto) {
        return CustomerEntity.builder()
                .email(customerDto.getEmail())
                .userName(customerDto.getUserName())
                .firstName(customerDto.getFirstName())
                .lastName(customerDto.getLastName())
                .created(new Timestamp(System.currentTimeMillis()))
                .build();
    }
}
