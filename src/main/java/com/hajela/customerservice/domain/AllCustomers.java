package com.hajela.customerservice.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AllCustomers {
    private List<CustomerDto> customers;
    private int totalItems;
    private int totalPages;
}
