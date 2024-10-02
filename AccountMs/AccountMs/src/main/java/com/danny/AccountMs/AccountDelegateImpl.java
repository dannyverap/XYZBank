package com.danny.AccountMs;

import com.danny.AccountMs.api.AccountApiDelegate;
import com.danny.AccountMs.business.AccountService;
import com.danny.AccountMs.model.*;
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
        return AccountApiDelegate.super.deleteAccount(id);
    }

    @Override
    public ResponseEntity<ModelApiResponse> depositToAccount(UUID id, Money money) {
        return AccountApiDelegate.super.depositToAccount(id, money);
    }

    @Override
    public ResponseEntity<AccountResponse> findAccountById(UUID id) {
        return AccountApiDelegate.super.findAccountById(id);
    }

    @Override
    public ResponseEntity<List<AccountResponse>> findAccounts(Integer offset, Integer limit) {
        return AccountApiDelegate.super.findAccounts(offset, limit);
    }

    @Override
    public ResponseEntity<AccountResponse> updateAccount(UUID id, AccountRequest accountRequest) {
        return AccountApiDelegate.super.updateAccount(id, accountRequest);
    }

    @Override
    public ResponseEntity<ModelApiResponse> withdrawFromAccount(UUID id, Money money) {
        return AccountApiDelegate.super.withdrawFromAccount(id, money);
    }
}