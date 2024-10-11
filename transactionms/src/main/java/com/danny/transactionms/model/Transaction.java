package com.danny.transactionms.model;

import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "transactions")
public class Transaction {

  @Id
  private String id;
  private TipoTransaction tipo;
  private Double monto;
  private Date fecha;
  private String cuentaOrigen;
  private String cuentaDestino;
}
