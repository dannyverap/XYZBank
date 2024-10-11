package com.danny.transactionms.business;

import com.danny.transactionms.exception.BadPetitionException;
import com.danny.transactionms.model.TipoTransaction;
import com.danny.transactionms.model.Transaction;
import com.danny.transactionms.model.TransactionRequest;
import com.danny.transactionms.model.TransactionResponse;
import java.time.ZoneId;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

  public Transaction getTransactionFromRequest(TransactionRequest transactionRequest) {
    Transaction transaction = new Transaction();
    transaction.setMonto(Math.abs(transactionRequest.getMonto()));
    transaction.setTipo(this.mapToEnumModel(transactionRequest.getTipo()));
    transaction.setFecha(
        Date.from(transactionRequest.getFecha().atStartOfDay(ZoneId.systemDefault()).toInstant()));
    transaction.setCuentaOrigen(transactionRequest.getCuentaOrigen());
    transaction.setCuentaDestino(transactionRequest.getCuentaDestino());
    return transaction;
  }

  public TransactionResponse getTransactionResponseFromTransaction(Transaction transaction) {
    TransactionResponse transactionResponse = new TransactionResponse();
    transactionResponse.setId(transaction.getId());
    transactionResponse.setMonto(Math.abs(transaction.getMonto()));
    transactionResponse.setTipo(this.mapToEnumResponse(transaction.getTipo()));
    transactionResponse.setFecha(
        transaction.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
    transactionResponse.setCuentaOrigen(transaction.getCuentaOrigen());
    transactionResponse.setCuentaDestino(transaction.getCuentaDestino());
    return transactionResponse;
  }

  public TransactionResponse.TipoEnum mapToEnumResponse(TipoTransaction tipoEnum) {
    if (tipoEnum.equals(TipoTransaction.TRANSFERENCIA)) {
      return TransactionResponse.TipoEnum.TRANSFERENCIA;
    }
    if (tipoEnum.equals(TipoTransaction.RETIRO)) {
      return TransactionResponse.TipoEnum.RETIRO;
    }
    if (tipoEnum.equals(TipoTransaction.DEPOSITO)) {
      return TransactionResponse.TipoEnum.DEPOSITO;
    }
    throw new BadPetitionException("Tipo operación no valido");
  }

  public TipoTransaction mapToEnumModel(TransactionRequest.TipoEnum tipoEnum) {
    if (tipoEnum.equals(TransactionRequest.TipoEnum.TRANSFERENCIA)) {
      return TipoTransaction.TRANSFERENCIA;
    }
    if (tipoEnum.equals(TransactionRequest.TipoEnum.RETIRO)) {
      return TipoTransaction.RETIRO;
    }
    if (tipoEnum.equals(TransactionRequest.TipoEnum.DEPOSITO)) {
      return TipoTransaction.DEPOSITO;
    }
    throw new BadPetitionException("Tipo operación no valido");
  }
}
