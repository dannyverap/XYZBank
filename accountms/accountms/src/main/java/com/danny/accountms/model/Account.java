package com.danny.accountms.model;

import com.danny.accountms.exception.BadPetitionException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  private String numeroCuenta;
  private Double saldo;
  private TipoCuenta tipoCuenta;
  private UUID clienteId;

  public void setClienteId(UUID clienteId) {
    if (clienteId == null) {
      throw new BadPetitionException("Proporcione un clienteId");
    }
    this.clienteId = clienteId;
  }
}
