package com.danny.accountms.business;

import com.danny.accountms.model.AccountRequest;
import com.danny.accountms.model.AccountResponse;
import com.danny.accountms.model.ModelApiResponse;
import com.danny.accountms.model.Money;
import com.danny.accountms.model.MoneyTransfer;
import java.util.List;
import java.util.UUID;

public interface AccountService {

  AccountResponse createAccount(AccountRequest accountRequest);

  List<AccountResponse> getAccounts(int limit, int offset, UUID clienteId);

  AccountResponse getAccountDetails(UUID id);

  AccountResponse updateAccount(UUID id, AccountRequest newAccountData);

  ModelApiResponse deleteAccount(UUID id);

  ModelApiResponse depositeMoneyToAccount(UUID id, Money money);

  ModelApiResponse withdrawMoneyFromAccount(UUID id, Money money);

  ModelApiResponse transferMoneyBetweenAccounts(UUID id, MoneyTransfer moneyTransfer);
}
