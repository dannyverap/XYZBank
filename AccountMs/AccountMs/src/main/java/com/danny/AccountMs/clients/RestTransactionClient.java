package com.danny.AccountMs.clients;

import com.danny.AccountMs.exception.BadPetitionException;
import com.danny.AccountMs.model.Money;
import com.danny.AccountMs.model.TransactionRequest;
import com.danny.AccountMs.model.TransactionResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Configuration
public class RestTransactionClient {
    private static final String Url = "http://localhost:8080/transaction/";

    @Bean
    public RestTemplate restTransactionTemplate() {
        return new RestTemplate();
    }

    public TransactionResponse registerDepositTransaction(Money money, String cuentaDestino) {
        TransactionRequest transactionRequest = this.createTransactionRequest(money,
                                                                              TransactionRequest.TipoEnum.DEPOSITO,
                                                                              cuentaDestino,
                                                                              "");
        return this.sendTransactionRequest(transactionRequest, "deposito");
    }

    public TransactionResponse registerWithdrawTransaction(Money money, String cuentaDestino) {
        TransactionRequest transactionRequest = this.createTransactionRequest(money,
                                                                              TransactionRequest.TipoEnum.RETIRO,
                                                                              cuentaDestino,
                                                                              "");
        return this.sendTransactionRequest(transactionRequest, "retiro");
    }

    public TransactionResponse registerTransferTransaction(Money money, String cuentaDestino, String cuentaOrigen) {
        TransactionRequest transactionRequest = this.createTransactionRequest(money,
                                                                              TransactionRequest.TipoEnum.TRANSFERENCIA,
                                                                              cuentaDestino,
                                                                              cuentaOrigen);
        return this.sendTransactionRequest(transactionRequest, "transferencia");
    }

    public TransactionResponse sendTransactionRequest(TransactionRequest transactionRequest, String operation) {
        try {
            return this.restTransactionTemplate().postForEntity(this.Url + operation, transactionRequest, TransactionResponse.class)
                                    .getBody();
            return this.restTransactionTemplate()
                       .postForEntity(this.Url + operation, transactionRequest, TransactionResponse.class)
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar la transacción", e);
        }
    }

    private TransactionRequest createTransactionRequest(Money money,
                                                        TransactionRequest.TipoEnum tipoTransaction,
                                                        String cuentaDestino,
                                                        String cuentaOrigen) {

        if (tipoTransaction.equals(TransactionRequest.TipoEnum.TRANSFERENCIA) && (cuentaOrigen == null || cuentaOrigen.isBlank())) {
            throw new BadPetitionException("Ingrese la cuenta de Origen");
        }
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setMonto(money.getDinero());
        transactionRequest.setCuentaDestino(cuentaDestino);
        transactionRequest.setCuentaOrigen(cuentaOrigen);
        transactionRequest.setTipo(tipoTransaction);
        transactionRequest.setFecha(LocalDate.now());

        return transactionRequest;
    }
}
