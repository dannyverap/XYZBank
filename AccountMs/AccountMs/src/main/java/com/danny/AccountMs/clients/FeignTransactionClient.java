package com.danny.AccountMs.clients;

import com.danny.AccountMs.model.TransactionRequest;
import com.danny.AccountMs.model.TransactionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "TRANSACTIONMS")
public interface FeignTransactionClient {
    @PostMapping("/transaction/deposito")
    TransactionResponse registerDepositTransaction(TransactionRequest transactionRequest);

    @PostMapping("/transaction/retiro")
    TransactionResponse registerWithdrawTransaction(TransactionRequest transactionRequest);

    @PostMapping("/transaction/transferencia")
    TransactionResponse registerTransferTransaction(TransactionRequest transactionRequest);
}
