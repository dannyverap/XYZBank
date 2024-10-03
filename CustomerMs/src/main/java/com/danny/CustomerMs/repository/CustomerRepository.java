package com.danny.CustomerMs.repository;

import com.danny.CustomerMs.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    boolean existsByEmail(String email);
    boolean existsByDni(String dni);
}
