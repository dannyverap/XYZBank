package com.danny.AccountMs.business;

import com.danny.AccountMs.clients.CustomerClient;
import com.danny.AccountMs.clients.TransactionClient;
import com.danny.AccountMs.exception.BadPetitionException;
import com.danny.AccountMs.model.*;
import com.danny.AccountMs.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Spy
    private AccountMapper accountMapper;

    @Mock
    private CustomerClient customerClient;

    @Mock
    private TransactionClient transactionClient;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account account;
    private AccountRequest accountRequest;
    private AccountResponse accountResponse;

    @BeforeEach
    public void setUp() {
        accountRequest = createAccountRequest();
        account = createAccount(accountRequest);
        accountResponse = createAccountResponse(account);
    }

    @Test
    @DisplayName("Test crear cuenta")
    public void testCreateAccount() {
        doNothing().when(customerClient).validateCustomerId(accountRequest.getClienteId());

        given(accountRepository.save(any(Account.class))).willReturn(account);
        AccountResponse response = accountService.createAccount(accountRequest);

        assertNotNull(response);
    }

    @Test
    @DisplayName("Test crear cuenta - Arroja error cuando no se encuentra el cliente")
    public void testCreateAccount_ThrowsErrorWhenCustomerNotFound() {

        doThrow(new BadPetitionException("El cliente no fue encontrado en el servicio de cuentas")).when(customerClient)
                                                                                                   .validateCustomerId(
                                                                                                           any(UUID.class));

        BadPetitionException exception = assertThrows(BadPetitionException.class, () -> {
            accountService.createAccount(accountRequest);
        });

        assertEquals("El cliente no fue encontrado en el servicio de cuentas", exception.getMessage());
    }


    @Test
    @DisplayName("Test obtener detalles de una cuenta")
    public void testGetAccountDetails() {
        given(accountRepository.findById(account.getId())).willReturn(Optional.of(account));


        AccountResponse response = accountService.getAccountDetails(account.getId());

        assertNotNull(response);
        assertEquals(account.getId(), response.getId());
    }

    @Test
    @DisplayName("Test listar cuentas - con clienteId")
    public void testGetAccountsWithClienteId() {
        UUID clienteId = UUID.randomUUID();
        List<Account> accountList = List.of(account);
        Page<Account> accountPage = new PageImpl<>(accountList);


        given(accountRepository.findByClienteId(clienteId, PageRequest.of(0, 20))).willReturn(accountPage);


        List<AccountResponse> responses = accountService.getAccounts(20, 0, clienteId);


        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(accountResponse, responses.get(0));
    }

    @Test
    @DisplayName("Test listar cuentas - sin clienteId")
    public void testGetAccountsWithoutClienteId() {
        List<Account> accountList = List.of(account);
        Page<Account> accountPage = new PageImpl<>(accountList);


        given(accountRepository.findAll(PageRequest.of(0, 20))).willReturn(accountPage);


        List<AccountResponse> responses = accountService.getAccounts(20, 0, null);


        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(accountResponse, responses.get(0));
    }

    @Test
    @DisplayName("Test actualizar cuenta")
    public void testUpdateAccount() {
        given(accountRepository.findById(account.getId())).willReturn(Optional.of(account));

        given(accountRepository.save(account)).willReturn(account);

        AccountResponse updatedAccount = accountService.updateAccount(account.getId(), accountRequest);
        assertNotNull(updatedAccount);
        assertEquals(account.getSaldo(), updatedAccount.getSaldo());
    }

    @Test
    @DisplayName("Test eliminar cuenta con saldo positivo - debería lanzar excepción")
    public void testDeleteAccountWithPositiveBalance() {
        account.setSaldo(300.0);
        given(accountRepository.findById(account.getId())).willReturn(Optional.of(account));

        assertThrows(BadPetitionException.class, () -> accountService.deleteAccount(account.getId()));
    }

    @Test
    @DisplayName("Test eliminar cuenta con saldo negativo - debería lanzar excepción")
    public void testDeleteAccountWithNegativeBalance() {
        given(accountRepository.findById(account.getId())).willReturn(Optional.of(account));

        account.setSaldo(-600.0);

        assertThrows(BadPetitionException.class, () -> accountService.deleteAccount(account.getId()));
    }

    @Test
    @DisplayName("Test eliminar cuenta con saldo 0 - No debería lanzar excepción")
    public void testDeleteAccountZeroBalance() {
        given(accountRepository.findById(account.getId())).willReturn(Optional.of(account));

        account.setSaldo(0.0);

        assertDoesNotThrow(() -> accountService.deleteAccount(account.getId()));
    }


    @Test
    @DisplayName("Test depositar dinero en cuenta")
    public void testDepositMoneyToAccount() {

        Money money = new Money();
        double amountToDeposit = 100.0;
        money.setDinero(amountToDeposit);

        double initialBalance = 200.0;
        account.setSaldo(initialBalance);

        given(accountRepository.findById(account.getId())).willReturn(Optional.of(account));
        given(transactionClient.registerDepositTransaction(any(Money.class),
                                                           anyString())).willReturn(new TransactionResponse());


        ModelApiResponse response = accountService.depositeMoneyToAccount(account.getId(), money);

        assertNotNull(response);
        assertTrue(response.getMessage().contains("Operación exitosa"));

        double expectedBalance = initialBalance + amountToDeposit;
        assertEquals(expectedBalance, account.getSaldo());
    }

    @Test
    @DisplayName("Test Retirar dinero en cuenta")
    public void testWithdrawMoneyToAccount() {

        Money money = new Money();
        double amountToWithdraw = 500.0;
        money.setDinero(amountToWithdraw);

        double initialBalance = 800;
        account.setSaldo(initialBalance);

        given(accountRepository.findById(account.getId())).willReturn(Optional.of(account));
        given(transactionClient.registerWithdrawTransaction(any(Money.class),
                                                            anyString())).willReturn(new TransactionResponse());


        ModelApiResponse response = accountService.withdrawMoneyFromAccount(account.getId(), money);

        assertNotNull(response);
        assertTrue(response.getMessage().contains("Operación exitosa"));

        double expectedBalance = initialBalance - amountToWithdraw;
        assertEquals(expectedBalance, account.getSaldo());
    }

    @Test
    @DisplayName("Test retirar dinero de la cuenta con saldo insuficiente - Debería lanzar excepción")
    public void testWithdrawMoneyInsufficientBalance() {
        Money money = new Money();
        money.setDinero(600.0);

        given(accountRepository.findById(account.getId())).willReturn(Optional.of(account));

        assertThrows(BadPetitionException.class, () -> accountService.withdrawMoneyFromAccount(account.getId(), money));
    }

    @Test
    @DisplayName("Test retirar dinero de cuenta Corriente con saldo mayor a -500")
    public void testWithdrawMoneyFromCorrienteAccountWithBalanceGreaterThanMinus500() {

        Money money = new Money();
        double amountToWithdraw = 500.0;
        money.setDinero(amountToWithdraw);

        double initialBalance = 0;
        account.setSaldo(initialBalance);
        account.setTipoCuenta(TipoCuenta.CORRIENTE);

        given(accountRepository.findById(account.getId())).willReturn(Optional.of(account));
        given(transactionClient.registerWithdrawTransaction(any(Money.class),
                                                            anyString())).willReturn(new TransactionResponse());


        ModelApiResponse response = accountService.withdrawMoneyFromAccount(account.getId(), money);

        assertNotNull(response);
        assertTrue(response.getMessage().contains("Operación exitosa"));

        double expectedBalance = initialBalance - amountToWithdraw;
        assertEquals(expectedBalance, account.getSaldo());
    }


    @Test
    @DisplayName("Test transferir dinero entre cuentas")
    public void testTransferMoneyBetweenAccounts() {
        account.setSaldo(200.0);

        MoneyTransfer moneyTransfer = new MoneyTransfer();
        moneyTransfer.setDinero(50.0);
        moneyTransfer.setIdCuentaDestino(UUID.randomUUID());

        Account accountDestination = createDestinationAccount(moneyTransfer);
        prepareTransferMocks(moneyTransfer, accountDestination);

        ModelApiResponse response = accountService.transferMoneyBetweenAccounts(account.getId(), moneyTransfer);
        assertNotNull(response);
        assertTrue(response.getMessage().contains("Operación exitosa"));
        assertEquals(150.0, account.getSaldo(), 0.001);
        assertEquals(50.0, accountDestination.getSaldo(), 0.001);
    }

    private AccountRequest createAccountRequest() {
        AccountRequest request = new AccountRequest();
        request.setSaldo(0.0);
        request.setTipoCuenta(AccountRequest.TipoCuentaEnum.AHORRO);
        request.setClienteId(UUID.randomUUID());
        return request;
    }

    private Account createAccount(AccountRequest request) {
        Account acc = new Account();
        acc.setId(UUID.randomUUID());
        acc.setSaldo(request.getSaldo());
        acc.setTipoCuenta(TipoCuenta.AHORRO);
        acc.setNumeroCuenta("123456");
        acc.setClienteId(request.getClienteId());
        return acc;
    }

    private AccountResponse createAccountResponse(Account account) {
        AccountResponse response = new AccountResponse();
        response.setId(account.getId());
        response.setSaldo(account.getSaldo());
        response.setTipoCuenta(AccountResponse.TipoCuentaEnum.AHORRO);
        response.setNumeroCuenta(account.getNumeroCuenta());
        response.setClienteId(account.getClienteId());
        return response;
    }

    private Account createDestinationAccount(MoneyTransfer moneyTransfer) {
        Account acc = new Account();
        acc.setId(moneyTransfer.getIdCuentaDestino());
        acc.setSaldo(0.0);
        acc.setNumeroCuenta("654321");
        return acc;
    }

    private void prepareTransferMocks(MoneyTransfer moneyTransfer, Account accountDestination) {
        given(accountRepository.findById(account.getId())).willReturn(Optional.of(account));
        given(accountRepository.findById(moneyTransfer.getIdCuentaDestino())).willReturn(Optional.of(accountDestination));

        Money money = new Money();
        money.setDinero(moneyTransfer.getDinero());

        given(transactionClient.registerTransferTransaction(any(Money.class),
                                                            eq(accountDestination.getNumeroCuenta()),
                                                            eq(account.getNumeroCuenta()))).willReturn(new TransactionResponse());
    }


}
