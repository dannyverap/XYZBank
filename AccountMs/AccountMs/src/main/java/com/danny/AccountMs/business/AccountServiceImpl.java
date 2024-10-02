package com.danny.AccountMs.business;

import com.danny.AccountMs.model.*;
import com.danny.AccountMs.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountMapper accountMapper;

    @Override
    public AccountResponse createAccount(AccountRequest accountRequest) {
        //Validar si usuario existe
        //luego crear cuenta
        Account account = this.accountMapper.getAccountFromRequest(accountRequest);
        account.setSaldo(0.0);
        account.setClientId(accountRequest.getClienteId());
        account.setNumeroCuenta(this.generateUniqueNumeroCuenta(account.getClientId(), account.getTipoCuenta()));
        this.accountRepository.save(account);
        return this.accountMapper.getAccountResponseFromAccount(account);
    }

    @Override
    public List<AccountResponse> getAccounts(int Limit, int offset) {
        return List.of();
    }

    @Override
    public AccountResponse getAccountDetails(UUID id) {
        return null;
    }

    @Override
    public AccountResponse updateAccount(UUID id, AccountRequest newAccountData) {
        return null;
    }

    @Override
    public ModelApiResponse deleteAccount(UUID id) {
        return null;
    }

    @Override
    public ModelApiResponse depositeMoneyToAccount(UUID id, Money money) {
        return null;
    }

    @Override
    public ModelApiResponse withdrawMoneyFromAccount(UUID id, Money money) {
        return null;
    }

    private String generateUniqueNumeroCuenta(UUID clientId, TipoCuenta tipoCuenta) {
        String numeroCuenta;
        do {
            numeroCuenta = this.generateRandomeNumeroCuenta(clientId, tipoCuenta);
        } while (this.accountRepository.existsByNumeroCuenta(numeroCuenta));
        return numeroCuenta;
    }

    private String generateRandomeNumeroCuenta(UUID clientId, TipoCuenta tipoCuenta) {
        Random random = new Random();
        return clientId.toString().substring(0, 4) + tipoCuenta.toString().substring(0, 3) + random.nextInt(1000);
    }
}
