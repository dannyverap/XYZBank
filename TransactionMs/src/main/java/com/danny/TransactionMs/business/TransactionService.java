package com.danny.TransactionMs.business;

import com.danny.TransactionMs.model.TransactionRequest;
import com.danny.TransactionMs.model.TransactionResponse;

import java.util.List;

public interface TransactionService {
    TransactionResponse registerDeposit(TransactionRequest depositeRequest);

    TransactionResponse registerWithdraw(TransactionRequest withDrawRequest);

    TransactionResponse registerTransfer(TransactionRequest transferRequest);

    List<TransactionResponse> getTransactions();
}
