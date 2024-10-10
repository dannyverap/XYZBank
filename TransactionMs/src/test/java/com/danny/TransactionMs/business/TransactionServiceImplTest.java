package com.danny.TransactionMs.business;

import com.danny.TransactionMs.exception.BadPetitionException;
import com.danny.TransactionMs.model.TipoTransaction;
import com.danny.TransactionMs.model.Transaction;
import com.danny.TransactionMs.model.TransactionRequest;
import com.danny.TransactionMs.model.TransactionResponse;
import com.danny.TransactionMs.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private TransactionRequest depositRequest;
    private TransactionRequest withdrawRequest;
    private TransactionRequest transferRequest;
    private Transaction depositTransaction;
    private Transaction withdrawTransaction;
    private Transaction transferTransaction;
    private TransactionResponse depositResponse;
    private TransactionResponse withdrawResponse;
    private TransactionResponse transferResponse;

    @BeforeEach
    public void setUp() {

        depositRequest = createDepositRequest();
        depositTransaction = createDepositTransaction(depositRequest);
        depositResponse = createDepositResponse(depositTransaction);

        withdrawRequest = createWithdrawRequest();
        withdrawTransaction = createWithdrawTransaction(withdrawRequest);
        withdrawResponse = createWithdrawResponse(withdrawTransaction);

        transferRequest = createTransferRequest();
        transferTransaction = createTransferTransaction(transferRequest);
        transferResponse = createTransferResponse(transferTransaction);
    }


    @Test
    @DisplayName("Test registrar depósito")
    public void testRegisterDeposit() {
        given(transactionMapper.getTransactionFromRequest(depositRequest)).willReturn(depositTransaction);
        given(transactionRepository.save(any(Transaction.class))).willReturn(depositTransaction);
        given(transactionMapper.getTransactionResponseFromTransaction(depositTransaction)).willReturn(depositResponse);

        TransactionResponse response = transactionService.registerDeposit(depositRequest);

        assertNotNull(response);
        assertEquals(depositResponse, response);
    }

    @Test
    @DisplayName("Test registrar depósito - Arroja error cuando el monto es nulo")
    public void testRegisterDeposit_ThrowsErrorWhenMontoIsNull() {
        depositRequest.setMonto(null);

        BadPetitionException exception = assertThrows(BadPetitionException.class,
                                                      () -> transactionService.registerDeposit(depositRequest));

        assertEquals("Introduzca un monto", exception.getMessage());
    }


    @Test
    @DisplayName("Test registrar depósito - Arroja error cuando cuenta destino es nula")
    public void testRegisterDeposit_ThrowsErrorWhenCuentaDestinoIsEmpty() {
        depositRequest.setCuentaDestino("");

        BadPetitionException exception = assertThrows(BadPetitionException.class,
                                                      () -> transactionService.registerDeposit(depositRequest));

        assertEquals("Introduzca cuenta de destino", exception.getMessage());
    }


    @Test
    @DisplayName("Test registrar retiro")
    public void testRegisterWithdraw() {
        given(transactionMapper.getTransactionFromRequest(withdrawRequest)).willReturn(withdrawTransaction);
        given(transactionRepository.save(any(Transaction.class))).willReturn(withdrawTransaction);
        given(transactionMapper.getTransactionResponseFromTransaction(withdrawTransaction)).willReturn(withdrawResponse);

        TransactionResponse response = transactionService.registerWithdraw(withdrawRequest);

        assertNotNull(response);
        assertEquals(withdrawResponse, response);
    }

    @Test
    @DisplayName("Test registrar retiro - Arroja error cuando el monto es nulo")
    public void testRegisterWithdraw_ThrowsErrorWhenMontoIsNull() {
        withdrawRequest.setMonto(null);

        BadPetitionException exception = assertThrows(BadPetitionException.class,
                                                      () -> transactionService.registerWithdraw(withdrawRequest));

        assertEquals("Introduzca un monto", exception.getMessage());
    }


    @Test
    @DisplayName("Test registrar retiro - Arroja error cuando cuenta destino es nula")
    public void testRegisterWithdraw_ThrowsErrorWhenCuentaDestinoIsEmpty() {
        withdrawRequest.setCuentaDestino("");

        BadPetitionException exception = assertThrows(BadPetitionException.class,
                                                      () -> transactionService.registerWithdraw(withdrawRequest));

        assertEquals("Introduzca cuenta de destino", exception.getMessage());
    }

    @Test
    @DisplayName("Test registrar transferencia")
    public void testRegisterTransfer() {
        given(transactionMapper.getTransactionFromRequest(transferRequest)).willReturn(transferTransaction);
        given(transactionRepository.save(any(Transaction.class))).willReturn(transferTransaction);
        given(transactionMapper.getTransactionResponseFromTransaction(transferTransaction)).willReturn(transferResponse);

        TransactionResponse response = transactionService.registerTransfer(transferRequest);

        assertNotNull(response);
        assertEquals(transferResponse, response);
    }

    @Test
    @DisplayName("Test registrar transferencia - Arroja error cuando el monto es nulo")
    public void testRegisterTransfer_ThrowsErrorWhenMontoIsNull() {
        transferRequest.setMonto(null);

        BadPetitionException exception = assertThrows(BadPetitionException.class,
                                                      () -> transactionService.registerTransfer(transferRequest));

        assertEquals("Introduzca un monto", exception.getMessage());
    }


    @Test
    @DisplayName("Test registrar transferencia - Arroja error cuando cuenta destino es nula")
    public void testRegisterTransfer_ThrowsErrorWhenCuentaDestinoIsEmpty() {
        transferRequest.setCuentaDestino("");

        BadPetitionException exception = assertThrows(BadPetitionException.class,
                                                      () -> transactionService.registerTransfer(transferRequest));

        assertEquals("Introduzca cuenta de destino", exception.getMessage());
    }

    @Test
    @DisplayName("Test registrar transferencia - Arroja error cuando cuenta origen es nula")
    public void testRegisterTransfer_ThrowsErrorWhenCuentaOrigenIsEmpty() {
        transferRequest.setCuentaOrigen("");

        BadPetitionException exception = assertThrows(BadPetitionException.class,
                                                      () -> transactionService.registerTransfer(transferRequest));

        assertEquals("Introduzca cuenta de origen", exception.getMessage());
    }


    private TransactionRequest createDepositRequest() {
        TransactionRequest request = new TransactionRequest();
        request.setMonto(100.0);
        request.setCuentaDestino("123456");
        return request;
    }

    private Transaction createDepositTransaction(TransactionRequest request) {
        Transaction transaction = new Transaction();
        transaction.setMonto(request.getMonto());
        transaction.setCuentaDestino(request.getCuentaDestino());
        transaction.setTipo(TipoTransaction.DEPOSITO);
        return transaction;
    }

    private TransactionResponse createDepositResponse(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setMonto(transaction.getMonto());
        response.setCuentaDestino(transaction.getCuentaDestino());
        return response;
    }

    private TransactionRequest createWithdrawRequest() {
        TransactionRequest request = new TransactionRequest();
        request.setMonto(50.0);
        request.setCuentaDestino("123456");
        return request;
    }

    private Transaction createWithdrawTransaction(TransactionRequest request) {
        Transaction transaction = new Transaction();
        transaction.setMonto(request.getMonto());
        transaction.setCuentaDestino(request.getCuentaDestino());
        transaction.setTipo(TipoTransaction.RETIRO);
        return transaction;
    }

    private TransactionResponse createWithdrawResponse(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setMonto(transaction.getMonto());
        response.setCuentaDestino(transaction.getCuentaDestino());
        return response;
    }

    private TransactionRequest createTransferRequest() {
        TransactionRequest request = new TransactionRequest();
        request.setMonto(200.0);
        request.setCuentaDestino("654321");
        request.setCuentaOrigen("123456");
        return request;
    }

    private Transaction createTransferTransaction(TransactionRequest request) {
        Transaction transaction = new Transaction();
        transaction.setMonto(request.getMonto());
        transaction.setCuentaDestino(request.getCuentaDestino());
        transaction.setCuentaOrigen(request.getCuentaOrigen());
        transaction.setTipo(TipoTransaction.TRANSFERENCIA);
        return transaction;
    }

    private TransactionResponse createTransferResponse(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setMonto(transaction.getMonto());
        response.setCuentaDestino(transaction.getCuentaDestino());
        response.setCuentaOrigen(transaction.getCuentaOrigen());
        return response;
    }
}
