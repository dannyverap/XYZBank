package com.danny.AccountMs;

import com.danny.AccountMs.api.AccountApiDelegate;
import com.danny.AccountMs.business.AccountService;
import com.danny.AccountMs.model.AccountRequest;
import com.danny.AccountMs.model.AccountResponse;
import com.danny.AccountMs.model.ModelApiResponse;
import com.danny.AccountMs.model.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class AccountDelegateImpl implements AccountApiDelegate {
    @Autowired
    AccountService accountService;

    @Override
    public ResponseEntity<AccountResponse> createAccount(AccountRequest accountRequest) {
        return ResponseEntity.ok(this.accountService.createAccount(accountRequest));
    }

    @Override
    public ResponseEntity<ModelApiResponse> deleteAccount(UUID id) {
        return ResponseEntity.ok(this.accountService.deleteAccount(id));
    }

    @Override
    public ResponseEntity<ModelApiResponse> depositToAccount(UUID id, Money money) {
        return ResponseEntity.ok(this.accountService.depositeMoneyToAccount(id, money));
    }

    @Override
    public ResponseEntity<AccountResponse> findAccountById(UUID id) {
        return ResponseEntity.ok(this.accountService.getAccountDetails(id));
    }

    @Override
    public ResponseEntity<List<AccountResponse>> findAccounts(Integer limit, Integer offset, UUID clienteId) {
        return ResponseEntity.ok(this.accountService.getAccounts(limit, offset, clienteId));
    }

    @Override
    public ResponseEntity<AccountResponse> updateAccount(UUID id, AccountRequest accountRequest) {
        return ResponseEntity.ok(this.accountService.updateAccount(id, accountRequest));
    }

    @Override
    public ResponseEntity<ModelApiResponse> withdrawFromAccount(UUID id, Money money) {
        return ResponseEntity.ok(this.accountService.withdrawMoneyFromAccount(id, money));
    }
}