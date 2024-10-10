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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Spy
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private TransactionRequest depositRequest;
    private TransactionRequest withdrawRequest;
    private TransactionRequest transferRequest;
    private Transaction depositTransaction;
    private Transaction withdrawTransaction;
    private Transaction transferTransaction;


    @BeforeEach
    public void setUp() {

        depositRequest = createDepositRequest();
        depositTransaction = createDepositTransaction(depositRequest);


        withdrawRequest = createWithdrawRequest();
        withdrawTransaction = createWithdrawTransaction(withdrawRequest);


        transferRequest = createTransferRequest();
        transferTransaction = createTransferTransaction(transferRequest);

    }


    @Test
    @DisplayName("Test registrar depósito")
    public void testRegisterDeposit() {
        given(transactionRepository.save(any(Transaction.class))).willReturn(depositTransaction);

        TransactionResponse response = transactionService.registerDeposit(depositRequest);

        assertNotNull(response);
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

        given(transactionRepository.save(any(Transaction.class))).willReturn(withdrawTransaction);

        TransactionResponse response = transactionService.registerWithdraw(withdrawRequest);
        assertNotNull(response);
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

        given(transactionRepository.save(any(Transaction.class))).willReturn(transferTransaction);

        TransactionResponse response = transactionService.registerTransfer(transferRequest);

        assertNotNull(response);
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
        request.setFecha(LocalDate.now());
        request.setTipo(TransactionRequest.TipoEnum.DEPOSITO);
        request.setMonto(100.0);
        request.setCuentaDestino("123456");
        return request;
    }

    private Transaction createDepositTransaction(TransactionRequest request) {
        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID().toString());
        transaction.setFecha(Date.from(Instant.now()));
        transaction.setMonto(request.getMonto());
        transaction.setCuentaDestino(request.getCuentaDestino());
        transaction.setTipo(TipoTransaction.DEPOSITO);
        return transaction;
    }


    private TransactionRequest createWithdrawRequest() {
        TransactionRequest request = new TransactionRequest();
        request.setFecha(LocalDate.now());
        request.setTipo(TransactionRequest.TipoEnum.RETIRO);
        request.setMonto(50.0);
        request.setCuentaDestino("123456");
        return request;
    }

    private Transaction createWithdrawTransaction(TransactionRequest request) {
        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID().toString());
        transaction.setFecha(Date.from(Instant.now()));
        transaction.setMonto(request.getMonto());
        transaction.setCuentaDestino(request.getCuentaDestino());
        transaction.setTipo(TipoTransaction.RETIRO);
        return transaction;
    }


    private TransactionRequest createTransferRequest() {
        TransactionRequest request = new TransactionRequest();
        request.setTipo(TransactionRequest.TipoEnum.TRANSFERENCIA);
        request.setFecha(LocalDate.now());
        request.setMonto(200.0);
        request.setCuentaDestino("654321");
        request.setCuentaOrigen("123456");
        return request;
    }

    private Transaction createTransferTransaction(TransactionRequest request) {
        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID().toString());
        transaction.setFecha(Date.from(Instant.now()));
        transaction.setMonto(request.getMonto());
        transaction.setCuentaDestino(request.getCuentaDestino());
        transaction.setCuentaOrigen(request.getCuentaOrigen());
        transaction.setTipo(TipoTransaction.TRANSFERENCIA);
        return transaction;
    }

}
