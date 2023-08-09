package com.hajela.customerservice.repository;

import com.hajela.customerservice.entities.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {

    Optional<CustomerEntity> findByCustomerId(Integer customerId);
}
