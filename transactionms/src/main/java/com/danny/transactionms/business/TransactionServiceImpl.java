package com.danny.transactionms.business;

import com.danny.transactionms.exception.BadPetitionException;
import com.danny.transactionms.model.TipoTransaction;
import com.danny.transactionms.model.Transaction;
import com.danny.transactionms.model.TransactionRequest;
import com.danny.transactionms.model.TransactionResponse;
import com.danny.transactionms.repository.TransactionRepository;
import java.util.List;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

  @Autowired
  TransactionRepository transactionRepository;

  @Autowired
  TransactionMapper transactionMapper;

  @Override
  public TransactionResponse registerDeposit(TransactionRequest depositeRequest) {
    if (depositeRequest.getMonto() == null || depositeRequest.getMonto().isNaN()) {
      throw new BadPetitionException("Introduzca un monto");
    }
    if (depositeRequest.getCuentaDestino() == null || depositeRequest.getCuentaDestino()
        .isBlank()) {
      throw new BadPetitionException("Introduzca cuenta de destino");
    }
    Transaction transaction = this.transactionMapper.getTransactionFromRequest(depositeRequest);
    transaction.setTipo(TipoTransaction.DEPOSITO);
    transaction.setCuentaOrigen(null);
    DateTime a = DateTime.now();
    transaction.setFecha(a.toDate());
    return this.transactionMapper.getTransactionResponseFromTransaction(
        this.transactionRepository.save(transaction));
  }

  @Override
  public TransactionResponse registerWithdraw(TransactionRequest withDrawRequest) {
    if (withDrawRequest.getMonto() == null || withDrawRequest.getMonto().isNaN()) {
      throw new BadPetitionException("Introduzca un monto");
    }
    if (withDrawRequest.getCuentaDestino() == null || withDrawRequest.getCuentaDestino()
        .isBlank()) {
      throw new BadPetitionException("Introduzca cuenta de destino");
    }
    Transaction transaction = this.transactionMapper.getTransactionFromRequest(withDrawRequest);
    transaction.setTipo(TipoTransaction.RETIRO);
    transaction.setCuentaOrigen(null);
    return this.transactionMapper.getTransactionResponseFromTransaction(
        this.transactionRepository.save(transaction));
  }

  @Override
  public TransactionResponse registerTransfer(TransactionRequest transferRequest) {
    if (transferRequest.getMonto() == null || transferRequest.getMonto().isNaN()) {
      throw new BadPetitionException("Introduzca un monto");
    }
    if (transferRequest.getCuentaDestino() == null || transferRequest.getCuentaDestino()
        .isBlank()) {
      throw new BadPetitionException("Introduzca cuenta de destino");
    }
    if (transferRequest.getCuentaOrigen() == null || transferRequest.getCuentaOrigen().isBlank()) {
      throw new BadPetitionException("Introduzca cuenta de origen");
    }
    Transaction transaction = this.transactionMapper.getTransactionFromRequest(transferRequest);
    transaction.setTipo(TipoTransaction.TRANSFERENCIA);
    return this.transactionMapper.getTransactionResponseFromTransaction(
        this.transactionRepository.save(transaction));
  }

  @Override
  public List<TransactionResponse> getTransactions() {
    return this.transactionRepository.findAll()
        .stream()
        .map(this.transactionMapper::getTransactionResponseFromTransaction)
        .toList();
  }
}
