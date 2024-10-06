package com.danny.TransactionMs;

import com.danny.TransactionMs.api.TransactionApiDelegate;
import com.danny.TransactionMs.business.TransactionService;
import com.danny.TransactionMs.model.TransactionRequest;
import com.danny.TransactionMs.model.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionDelegateImpl implements TransactionApiDelegate {
    @Autowired
    TransactionService transactionService;

    @Override
    public ResponseEntity<List<TransactionResponse>> getHistorial() {
        return ResponseEntity.ok(this.transactionService.getTransactions());
    }

    @Override
    public ResponseEntity<TransactionResponse> registerDeposit(TransactionRequest transactionRequest) {
        return ResponseEntity.ok(this.transactionService.registerDeposit(transactionRequest));
    }

    @Override
    public ResponseEntity<TransactionResponse> registerTransfer(TransactionRequest transactionRequest) {
        return ResponseEntity.ok(this.transactionService.registerTransfer(transactionRequest));
    }

    @Override
    public ResponseEntity<TransactionResponse> registerWithdraw(TransactionRequest transactionRequest) {
        return ResponseEntity.ok(this.transactionService.registerWithdraw(transactionRequest));
    }
}
