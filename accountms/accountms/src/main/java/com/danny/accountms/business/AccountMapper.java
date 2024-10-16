package com.danny.accountms.business;

import com.danny.accountms.exception.BadPetitionException;
import com.danny.accountms.model.Account;
import com.danny.accountms.model.AccountRequest;
import com.danny.accountms.model.AccountResponse;
import com.danny.accountms.model.Money;
import com.danny.accountms.model.TipoCuenta;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

  public Account getAccountFromRequest(AccountRequest accountRequest) {
    Account account = new Account();
    account.setSaldo(accountRequest.getSaldo());
    account.setTipoCuenta(this.mapToEnumModel(accountRequest.getTipoCuenta()));
    account.setClienteId(accountRequest.getClienteId());
    account.setNumeroCuenta(accountRequest.getNumeroCuenta());
    return account;
  }

  public AccountResponse getAccountResponseFromAccount(Account account) {
    AccountResponse accountResponse = new AccountResponse();
    accountResponse.setId(account.getId());
    accountResponse.setSaldo(account.getSaldo());
    accountResponse.setTipoCuenta(this.mapToEnumResponse(account.getTipoCuenta()));
    accountResponse.setClienteId(account.getClienteId());
    accountResponse.setNumeroCuenta(account.getNumeroCuenta());
    return accountResponse;
  }

  public AccountResponse.TipoCuentaEnum mapToEnumResponse(TipoCuenta tipoEnum) {
    if (tipoEnum == null) {
      throw new BadPetitionException("Tipo de cuenta no puede ser null");
    }
    if (tipoEnum.equals(TipoCuenta.AHORRO)) {
      return AccountResponse.TipoCuentaEnum.AHORRO;
    }
    if (tipoEnum.equals(TipoCuenta.CORRIENTE)) {
      return AccountResponse.TipoCuentaEnum.CORRIENTE;
    }
    throw new BadPetitionException("Tipo de cuenta no valido");
  }

  public TipoCuenta mapToEnumModel(AccountRequest.TipoCuentaEnum tipoEnum) {
    if (tipoEnum == null) {
      throw new BadPetitionException("Tipo de cuenta no puede ser null");
    }
    if (tipoEnum.equals(AccountRequest.TipoCuentaEnum.AHORRO)) {
      return TipoCuenta.AHORRO;
    }
    if (tipoEnum.equals(AccountRequest.TipoCuentaEnum.CORRIENTE)) {
      return TipoCuenta.CORRIENTE;
    }
    throw new BadPetitionException("Tipo de cuenta no valido");
  }

  public Money convertMoneyTransferToMoney(Double moneyTransfer) {
    Money money = new Money();
    money.setDinero(moneyTransfer);
    return money;
  }
}

