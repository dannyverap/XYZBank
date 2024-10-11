package com.danny.transactionms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.danny.transactionms.business.TransactionService;
import com.danny.transactionms.model.TransactionRequest;
import com.danny.transactionms.model.TransactionResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class TransactionDelegateImplTest {

  @Mock
  TransactionService transactionService;

  @InjectMocks
  TransactionDelegateImpl transactionDelegate;

  private TransactionRequest transactionRequest;
  private TransactionResponse transactionResponse;

  @BeforeEach
  public void setUp() {
    transactionRequest = new TransactionRequest();
    transactionResponse = new TransactionResponse();
  }

  @Test
  public void getHistorialTest() {
    given(transactionService.getTransactions()).willReturn(List.of(transactionResponse));

    ResponseEntity<List<TransactionResponse>> response = transactionDelegate.getHistorial();

    assertNotNull(response.getBody());
    assertEquals(200, response.getStatusCode().value());
    assertEquals(1, response.getBody().size());
  }

  @Test
  public void createTransactionTest() {
    given(transactionService.registerDeposit(any(TransactionRequest.class))).willReturn(
        transactionResponse);

    ResponseEntity<TransactionResponse> response = transactionDelegate.registerDeposit(
        transactionRequest);

    assertNotNull(response.getBody());
    assertEquals(200, response.getStatusCode().value());
    assertEquals(transactionResponse, response.getBody());
  }

  @Test
  public void createWithdrawTest() {
    given(transactionService.registerWithdraw(any(TransactionRequest.class))).willReturn(
        transactionResponse);

    ResponseEntity<TransactionResponse> response = transactionDelegate.registerWithdraw(
        transactionRequest);

    assertNotNull(response.getBody());
    assertEquals(200, response.getStatusCode().value());
    assertEquals(transactionResponse, response.getBody());
  }

  @Test
  public void createTransferTest() {
    given(transactionService.registerTransfer(any(TransactionRequest.class))).willReturn(
        transactionResponse);

    ResponseEntity<TransactionResponse> response = transactionDelegate.registerTransfer(
        transactionRequest);

    assertNotNull(response.getBody());
    assertEquals(200, response.getStatusCode().value());
    assertEquals(transactionResponse, response.getBody());
  }
}
