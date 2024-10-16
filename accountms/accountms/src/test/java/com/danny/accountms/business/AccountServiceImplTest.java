package com.danny.accountms.business;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import com.danny.accountms.clients.CustomerClient;
import com.danny.accountms.clients.TransactionClient;
import com.danny.accountms.exception.BadPetitionException;
import com.danny.accountms.exception.NotFoundException;
import com.danny.accountms.model.Account;
import com.danny.accountms.model.AccountRequest;
import com.danny.accountms.model.AccountResponse;
import com.danny.accountms.model.ModelApiResponse;
import com.danny.accountms.model.Money;
import com.danny.accountms.model.MoneyTransfer;
import com.danny.accountms.model.TipoCuenta;
import com.danny.accountms.model.TransactionResponse;
import com.danny.accountms.repository.AccountRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    doThrow(
        new BadPetitionException("El cliente no fue encontrado en el servicio de cuentas")).when(
        customerClient).validateCustomerId(any(UUID.class));

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
  @DisplayName("Test obtener detalles de una cuenta - Arroja error cuando la cuenta no existe")
  public void testGetAccountDetailsT() {
    given(accountRepository.findById(account.getId())).willReturn(Optional.empty());

    NotFoundException exception = assertThrows(NotFoundException.class, () -> {
      accountService.getAccountDetails(account.getId());
    });

    assertEquals("Cuenta no encontrada", exception.getMessage());
  }

  @Test
  @DisplayName("Test listar cuentas - con clienteId")
  public void testGetAccountsWithClienteId() {
    UUID clienteId = UUID.randomUUID();
    List<Account> accountList = List.of(account);
    Page<Account> accountPage = new PageImpl<>(accountList);

    given(accountRepository.findByClienteId(clienteId, PageRequest.of(0, 20))).willReturn(
        accountPage);

    List<AccountResponse> responses = accountService.getAccounts(20, 0, clienteId);

    assertNotNull(responses);
    assertEquals(1, responses.size());
    assertEquals(accountResponse, responses.get(0));
  }

  @Test
  @DisplayName("Test listar cuentas - límite negativo")
  public void testGetAccountsWithNegativeLimit() {
    List<Account> accountList = List.of(account);
    Page<Account> accountPage = new PageImpl<>(accountList);

    given(accountRepository.findAll(PageRequest.of(0, 20))).willReturn(accountPage);
    given(accountMapper.getAccountResponseFromAccount(account)).willReturn(accountResponse);

    List<AccountResponse> responses = accountService.getAccounts(-5, 0, null);

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
    accountRequest.setNumeroCuenta("nttcuenta");
    accountRequest.setTipoCuenta(AccountRequest.TipoCuentaEnum.CORRIENTE);
    accountRequest.setSaldo(500.0);
    given(accountRepository.findById(account.getId())).willReturn(Optional.of(account));
    given(accountRepository.save(account)).willReturn(account);
    given(accountRepository.existsByNumeroCuenta(accountRequest.getNumeroCuenta())).willReturn(
        false);

    AccountResponse updatedAccount = accountService.updateAccount(account.getId(), accountRequest);
    assertNotNull(updatedAccount);
    assertEquals(account.getNumeroCuenta(), updatedAccount.getNumeroCuenta());
  }


  @Test
  @DisplayName("Test actualizar cuenta - arroja error cuando la cuenta no se encuentra")
  public void testUpdateAccountThrowsNotFoundExceptionWhenAccountNotFound() {
    UUID accountId = account.getId();

    given(accountRepository.findById(accountId)).willReturn(Optional.empty());

    NotFoundException exception = assertThrows(NotFoundException.class, () -> {
      accountService.updateAccount(accountId, accountRequest);
    });

    assertEquals("no encontrado", exception.getMessage());
  }

  @Test
  @DisplayName("Test actualizar cuenta - arroja error por número de cuenta duplicado")
  public void testUpdateAccountThrowsErrorWhenAccountNumberIsDuplicate() {
    UUID accountId = account.getId();
    accountRequest.setNumeroCuenta("654329");
    account.setNumeroCuenta("654328");
    given(accountRepository.findById(accountId)).willReturn(Optional.of(account));
    given(accountRepository.existsByNumeroCuenta(accountRequest.getNumeroCuenta())).willReturn(
        true);

    BadPetitionException exception = assertThrows(BadPetitionException.class, () -> {
      accountService.updateAccount(accountId, accountRequest);
    });

    assertEquals("El numero de cuenta debe ser única", exception.getMessage());
  }

  @Test
  @DisplayName("Test actualizar cuenta - no realiza cambios si los datos son iguales")
  public void testUpdateAccountNoChangeWhenDataIsEqual() {
    UUID accountId = account.getId();
    accountRequest.setNumeroCuenta(account.getNumeroCuenta());
    accountRequest.setSaldo(account.getSaldo());

    given(accountRepository.findById(accountId)).willReturn(Optional.of(account));
    given(accountRepository.save(any(Account.class))).willReturn(account);

    AccountResponse response = accountService.updateAccount(accountId, accountRequest);

    assertNotNull(response);
    assertEquals(account.getSaldo(), response.getSaldo());
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
    given(transactionClient.registerDepositTransaction(any(Money.class), anyString())).willReturn(
        new TransactionResponse());

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
    given(transactionClient.registerWithdrawTransaction(any(Money.class), anyString())).willReturn(
        new TransactionResponse());

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

    assertThrows(BadPetitionException.class,
        () -> accountService.withdrawMoneyFromAccount(account.getId(), money));
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
    given(transactionClient.registerWithdrawTransaction(any(Money.class), anyString())).willReturn(
        new TransactionResponse());

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
    given(accountRepository.findById(account.getId())).willReturn(Optional.of(account));
    given(accountRepository.findById(moneyTransfer.getIdCuentaDestino())).willReturn(
        Optional.of(accountDestination));

    Money money = new Money();
    money.setDinero(moneyTransfer.getDinero());

    given(transactionClient.registerTransferTransaction(any(Money.class),
        eq(accountDestination.getNumeroCuenta()), eq(account.getNumeroCuenta()))).willReturn(
        new TransactionResponse());

    ModelApiResponse response = accountService.transferMoneyBetweenAccounts(account.getId(),
        moneyTransfer);
    assertNotNull(response);
    assertTrue(response.getMessage().contains("Operación exitosa"));
    assertEquals(150.0, account.getSaldo(), 0.001);
    assertEquals(50.0, accountDestination.getSaldo(), 0.001);
  }

  @Test
  @DisplayName("Test transferir dinero entre cuentas - Arroja error si el dinero a transferir es 0")
  public void testTransferMoneyBetweenAccountsWithZeroAmount() {

    MoneyTransfer moneyTransfer = new MoneyTransfer();
    moneyTransfer.setDinero(0.0);
    moneyTransfer.setIdCuentaDestino(UUID.randomUUID());

    assertThrows(BadPetitionException.class,
        () -> accountService.transferMoneyBetweenAccounts(account.getId(), moneyTransfer));
  }

  @Test
  @DisplayName("Test transferir dinero entre cuentas - Arroja error si cuenta de origen tiene saldo es menor a 0")
  public void testTransferMoneyBetweenAccountsWithInsufficientBalance() {
    account.setSaldo(200.0);

    MoneyTransfer moneyTransfer = new MoneyTransfer();
    moneyTransfer.setDinero(800.0);
    moneyTransfer.setIdCuentaDestino(UUID.randomUUID());

    Account accountDestination = createDestinationAccount(moneyTransfer);
    given(accountRepository.findById(account.getId())).willReturn(Optional.of(account));
    given(accountRepository.findById(moneyTransfer.getIdCuentaDestino())).willReturn(
        Optional.of(accountDestination));

    Money money = new Money();
    money.setDinero(moneyTransfer.getDinero());
    assertThrows(BadPetitionException.class,
        () -> accountService.transferMoneyBetweenAccounts(account.getId(), moneyTransfer));
  }

  @Test
  @DisplayName("Test transferir dinero entre cuentas - Arroja error si cuenta de origen invalido")
  public void testTransferMoneyBetweenAccountsWithInvalidAccountOrigin() {
    account.setSaldo(200.0);

    MoneyTransfer moneyTransfer = new MoneyTransfer();
    moneyTransfer.setDinero(100.0);
    moneyTransfer.setIdCuentaDestino(UUID.randomUUID());

    given(accountRepository.findById(account.getId())).willReturn(Optional.empty());
    Money money = new Money();
    money.setDinero(moneyTransfer.getDinero());
    assertThrows(NotFoundException.class,
        () -> accountService.transferMoneyBetweenAccounts(account.getId(), moneyTransfer));
  }

  @Test
  @DisplayName("Test transferir dinero entre cuentas - Arroja error si cuenta de destino invalido")
  public void testTransferMoneyBetweenAccountsWithInvalidAccountDestination() {
    account.setSaldo(200.0);

    MoneyTransfer moneyTransfer = new MoneyTransfer();
    moneyTransfer.setDinero(100.0);
    moneyTransfer.setIdCuentaDestino(UUID.randomUUID());

    given(accountRepository.findById(account.getId())).willReturn(Optional.of(account));
    given(accountRepository.findById(moneyTransfer.getIdCuentaDestino())).willReturn(
        Optional.empty());

    Money money = new Money();
    money.setDinero(moneyTransfer.getDinero());
    assertThrows(NotFoundException.class,
        () -> accountService.transferMoneyBetweenAccounts(account.getId(), moneyTransfer));
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

}
