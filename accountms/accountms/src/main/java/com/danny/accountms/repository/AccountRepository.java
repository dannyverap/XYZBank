package com.danny.accountms.repository;

import com.danny.accountms.model.Account;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, UUID> {

  boolean existsByNumeroCuenta(String numeroCuenta);

  Page<Account> findByClienteId(UUID clienteId, Pageable pageable);
}
