package com.danny.TransactionMs.model;

import lombok.Data;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@Document(collection = "transactions")
public class Transaction {
    @Id
    private String id;
    private TipoTransaction tipo;
    private Double Monto;
    private Date fecha;
    private String cuentaOrigen;
    private String cuentaDestino;
}
