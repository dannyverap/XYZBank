package com.danny.AccountMs.business;

import com.danny.AccountMs.model.AccountRequest;
import com.danny.AccountMs.model.AccountResponse;
import com.danny.AccountMs.model.ModelApiResponse;
import com.danny.AccountMs.model.Money;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    AccountResponse createAccount (AccountRequest accountRequest);
    List<AccountResponse> getAccounts(int Limit, int offset);
    AccountResponse getAccountDetails(UUID id);
    AccountResponse updateAccount(UUID id, AccountRequest newAccountData);
    ModelApiResponse deleteAccount(UUID id);
    ModelApiResponse depositeMoneyToAccount(UUID id, Money money);
    ModelApiResponse withdrawMoneyFromAccount(UUID id, Money money);
}
