package com.danny.AccountMs.repository;

import com.danny.AccountMs.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    boolean existsByNumeroCuenta(String numeroCuenta);
    Page<Account> findByClienteId(UUID clienteId, Pageable pageable);
}
