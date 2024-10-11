package com.danny.transactionms.business;

import com.danny.transactionms.model.TransactionRequest;
import com.danny.transactionms.model.TransactionResponse;
import java.util.List;

public interface TransactionService {

  TransactionResponse registerDeposit(TransactionRequest depositeRequest);

  TransactionResponse registerWithdraw(TransactionRequest withDrawRequest);

  TransactionResponse registerTransfer(TransactionRequest transferRequest);

  List<TransactionResponse> getTransactions();
}
