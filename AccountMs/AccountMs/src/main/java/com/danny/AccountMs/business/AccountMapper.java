package com.danny.AccountMs.business;

import com.danny.AccountMs.exception.BadPetitionException;
import com.danny.AccountMs.model.Account;
import com.danny.AccountMs.model.AccountRequest;
import com.danny.AccountMs.model.AccountResponse;
import com.danny.AccountMs.model.TipoCuenta;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    public Account getAccountFromRequest(AccountRequest accountRequest) {
        Account account = new Account();
        account.setSaldo(accountRequest.getSaldo());
        account.setTipoCuenta(this.mapToEnumModel(accountRequest.getTipoCuenta()));
        account.setClientId(accountRequest.getClienteId());
        account.setNumeroCuenta(account.getNumeroCuenta());
        return account;
    }

    public AccountResponse getAccountResponseFromAccount(Account account) {
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setId(account.getId());
        accountResponse.setSaldo(account.getSaldo());
        accountResponse.setTipoCuenta(this.mapToEnumResponse(account.getTipoCuenta()));
        accountResponse.setClienteId(account.getClientId());
        accountResponse.setNumeroCuenta(account.getNumeroCuenta());
        return accountResponse;
    }

    public AccountResponse.TipoCuentaEnum mapToEnumResponse(TipoCuenta tipoEnum) {
        if (tipoEnum.equals(TipoCuenta.AHORRO)) {
            return AccountResponse.TipoCuentaEnum.AHORRO;
        }
        if (tipoEnum.equals(TipoCuenta.CORRIENTE)) {
            return AccountResponse.TipoCuentaEnum.CORRIENTE;
        }
        throw new BadPetitionException("Tipo de cuenta no valido");
    }

    public TipoCuenta mapToEnumModel(AccountRequest.TipoCuentaEnum tipoEnum) {
        if (tipoEnum.equals(AccountRequest.TipoCuentaEnum.AHORRO)) {
            return TipoCuenta.AHORRO;
        }
        if (tipoEnum.equals(AccountRequest.TipoCuentaEnum.CORRIENTE)) {
            return TipoCuenta.CORRIENTE;
        }
        throw new BadPetitionException("Tipo de cuenta no valido");
    }
};

