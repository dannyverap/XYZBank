package com.danny.AccountMs.business;

import com.danny.AccountMs.exception.BadPetitionException;
import com.danny.AccountMs.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountMapperTest {

    private AccountMapper accountMapper;

    @BeforeEach
    void setUp() {
        accountMapper = new AccountMapper();
    }

    @Test
    void testGetAccountFromRequest() {
        UUID id = UUID.randomUUID();
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setSaldo(500.0);
        accountRequest.setTipoCuenta(AccountRequest.TipoCuentaEnum.AHORRO);
        accountRequest.setClienteId(id);

        Account account = accountMapper.getAccountFromRequest(accountRequest);

        assertNotNull(account);
        assertEquals(500.0, account.getSaldo());
        assertEquals(TipoCuenta.AHORRO, account.getTipoCuenta());
        assertEquals(id, account.getClienteId());
    }

    @Test
    void testGetAccountResponseFromAccount() {

        UUID id = UUID.randomUUID();
        UUID clienteId = UUID.randomUUID();
        Account account = new Account();
        account.setId(id);
        account.setSaldo(1000.0);
        account.setTipoCuenta(TipoCuenta.CORRIENTE);
        account.setClienteId(clienteId);
        account.setNumeroCuenta("num-789");

        AccountResponse accountResponse = accountMapper.getAccountResponseFromAccount(account);

        assertNotNull(accountResponse);
        assertEquals(id, accountResponse.getId());
        assertEquals(1000.0, accountResponse.getSaldo());
        assertEquals(AccountResponse.TipoCuentaEnum.CORRIENTE, accountResponse.getTipoCuenta());
        assertEquals(clienteId, accountResponse.getClienteId());
        assertEquals("num-789", accountResponse.getNumeroCuenta());
    }

    @Test
    void testMapToEnumResponseAhorro() {

        AccountResponse.TipoCuentaEnum result = accountMapper.mapToEnumResponse(TipoCuenta.AHORRO);

        assertEquals(AccountResponse.TipoCuentaEnum.AHORRO, result);
    }

    @Test
    void testMapToEnumResponseCorriente() {
        AccountResponse.TipoCuentaEnum result = accountMapper.mapToEnumResponse(TipoCuenta.CORRIENTE);

        assertEquals(AccountResponse.TipoCuentaEnum.CORRIENTE, result);
    }

    @Test
    void testMapToEnumResponseInvalid() {

        TipoCuenta invalidTipo = null;

        assertThrows(BadPetitionException.class, () -> accountMapper.mapToEnumResponse(invalidTipo));
    }

    @Test
    void testMapToEnumModelAhorro() {

        TipoCuenta result = accountMapper.mapToEnumModel(AccountRequest.TipoCuentaEnum.AHORRO);


        assertEquals(TipoCuenta.AHORRO, result);
    }

    @Test
    void testMapToEnumModelCorriente() {
        TipoCuenta result = accountMapper.mapToEnumModel(AccountRequest.TipoCuentaEnum.CORRIENTE);

        assertEquals(TipoCuenta.CORRIENTE, result);
    }

    @Test
    void testMapToEnumModelInvalid() {
        AccountRequest.TipoCuentaEnum invalidTipo = null;

        assertThrows(BadPetitionException.class, () -> accountMapper.mapToEnumModel(invalidTipo));
    }

    @Test
    void testConvertMoneyTransferToMoney() {

        Double moneyTransfer = 200.0;


        Money money = accountMapper.convertMoneyTransferToMoney(moneyTransfer);


        assertNotNull(money);
        assertEquals(200.0, money.getDinero());
    }
}
