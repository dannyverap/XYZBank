package com.danny.TransactionMs.business;

import com.danny.TransactionMs.exception.BadPetitionException;
import com.danny.TransactionMs.model.*;
import com.danny.TransactionMs.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

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
    private Transaction depositTransaction;
    private TransactionResponse depositResponse;

    @BeforeEach
    public void setUp() {
        depositRequest = createDepositRequest();
        depositTransaction = createDepositTransaction(depositRequest);
        depositResponse = createDepositResponse(depositTransaction);
    }

    // Test para el registro de un depósito
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

    // Test para lanzar una excepción si el monto es nulo
    @Test
    @DisplayName("Test registrar depósito - Arroja error cuando el monto es nulo")
    public void testRegisterDeposit_ThrowsErrorWhenMontoIsNull() {
        depositRequest.setMonto(null);

        BadPetitionException exception = assertThrows(BadPetitionException.class,
                () -> transactionService.registerDeposit(depositRequest));

        assertEquals("Introduzca un monto", exception.getMessage());
    }

    // Test para lanzar una excepción si la cuenta destino es nula o está vacía
    @Test
    @DisplayName("Test registrar depósito - Arroja error cuando la cuenta destino es nula o vacía")
    public void testRegisterDeposit_ThrowsErrorWhenCuentaDestinoIsEmpty() {
        depositRequest.setCuentaDestino("");

        BadPetitionException exception = assertThrows(BadPetitionException.class,
                () -> transactionService.registerDeposit(depositRequest));

        assertEquals("Introduzca cuenta de destino", exception.getMessage());
    }

    // Test para obtener transacciones
    @Test
    @DisplayName("Test obtener transacciones")
    public void testGetTransactions() {
        List<Transaction> transactionList = List.of(depositTransaction);

        given(transactionRepository.findAll()).willReturn(transactionList);
        given(transactionMapper.getTransactionResponseFromTransaction(depositTransaction)).willReturn(depositResponse);

        List<TransactionResponse> responses = transactionService.getTransactions();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(depositResponse, responses.get(0));
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
}
