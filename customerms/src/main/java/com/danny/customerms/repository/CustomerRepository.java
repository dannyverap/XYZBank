package com.danny.customerms.repository;

import com.danny.customerms.model.Customer;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

  boolean existsByEmail(String email);

  boolean existsByDni(String dni);
}
