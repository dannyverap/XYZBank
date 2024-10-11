package com.danny.AccountMs;

import com.danny.AccountMs.business.AccountService;
import com.danny.AccountMs.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AccountDelegateImplTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountDelegateImpl accountDelegate;

    private AccountRequest accountRequest;
    private AccountResponse accountResponse;
    private Money money;
    private UUID accountId;

    @BeforeEach
    public void setUp() {
        accountId = UUID.randomUUID();
        accountRequest = new AccountRequest();
        accountResponse = new AccountResponse();
        money = new Money();
    }

    @Test
    @DisplayName("Test transferir dinero entre cuentas")
    void testTransferMoneyBetweenAccounts() {
        MoneyTransfer moneyTransfer = new MoneyTransfer();
        given(accountService.transferMoneyBetweenAccounts(any(UUID.class),
                                                          any(MoneyTransfer.class))).willReturn(new ModelApiResponse());

        ResponseEntity<ModelApiResponse> response = accountDelegate.transferFromAccount(accountId, moneyTransfer);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    @DisplayName("Test crear cuenta")
    void testCreateAccount() {
        given(accountService.createAccount(any(AccountRequest.class))).willReturn(accountResponse);

        ResponseEntity<AccountResponse> response = accountDelegate.createAccount(accountRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(accountResponse, response.getBody());
    }

    @Test
    @DisplayName("Test eliminar cuenta")
    void testDeleteAccount() {
        given(accountService.deleteAccount(any(UUID.class))).willReturn(new ModelApiResponse());

        ResponseEntity<ModelApiResponse> response = accountDelegate.deleteAccount(accountId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    @DisplayName("Test depositar dinero en cuenta")
    void testDepositToAccount() {
        given(accountService.depositeMoneyToAccount(any(UUID.class),
                                                    any(Money.class))).willReturn(new ModelApiResponse());

        ResponseEntity<ModelApiResponse> response = accountDelegate.depositToAccount(accountId, money);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    @DisplayName("Test buscar cuenta por ID")
    void testFindAccountById() {
        given(accountService.getAccountDetails(any(UUID.class))).willReturn(accountResponse);

        ResponseEntity<AccountResponse> response = accountDelegate.findAccountById(accountId);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(accountResponse, response.getBody());
    }

    @Test
    @DisplayName("Test buscar cuentas por clienteId")
    void testFindAccounts() {
        given(accountService.getAccounts(any(Integer.class), any(Integer.class), any(UUID.class))).willReturn(List.of(
                accountResponse));

        ResponseEntity<List<AccountResponse>> response = accountDelegate.findAccounts(20, 0, UUID.randomUUID());

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("Test actualizar cuenta")
    void testUpdateAccount() {
        given(accountService.updateAccount(any(UUID.class), any(AccountRequest.class))).willReturn(accountResponse);

        ResponseEntity<AccountResponse> response = accountDelegate.updateAccount(accountId, accountRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(accountResponse, response.getBody());
    }

    @Test
    @DisplayName("Test retirar dinero de la cuenta")
    void testWithdrawFromAccount() {
        given(accountService.withdrawMoneyFromAccount(any(UUID.class),
                                                      any(Money.class))).willReturn(new ModelApiResponse());

        ResponseEntity<ModelApiResponse> response = accountDelegate.withdrawFromAccount(accountId, money);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
    }
}
