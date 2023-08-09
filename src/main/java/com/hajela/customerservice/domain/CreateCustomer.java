package com.hajela.customerservice.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomer {
    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Size(min = 5, message = "Username should have at least 8 characters")
    private String userName;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;
}
