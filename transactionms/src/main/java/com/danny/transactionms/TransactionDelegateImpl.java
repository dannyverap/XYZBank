package com.danny.transactionms;

import com.danny.transactionms.api.TransactionApiDelegate;
import com.danny.transactionms.business.TransactionService;
import com.danny.transactionms.model.TransactionRequest;
import com.danny.transactionms.model.TransactionResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class TransactionDelegateImpl implements TransactionApiDelegate {

  @Autowired
  TransactionService transactionService;

  @Override
  public ResponseEntity<List<TransactionResponse>> getHistorial() {
    return ResponseEntity.ok(this.transactionService.getTransactions());
  }

  @Override
  public ResponseEntity<TransactionResponse> registerDeposit(
      TransactionRequest transactionRequest) {
    return ResponseEntity.ok(this.transactionService.registerDeposit(transactionRequest));
  }

  @Override
  public ResponseEntity<TransactionResponse> registerTransfer(
      TransactionRequest transactionRequest) {
    return ResponseEntity.ok(this.transactionService.registerTransfer(transactionRequest));
  }

  @Override
  public ResponseEntity<TransactionResponse> registerWithdraw(
      TransactionRequest transactionRequest) {
    return ResponseEntity.ok(this.transactionService.registerWithdraw(transactionRequest));
  }
}
